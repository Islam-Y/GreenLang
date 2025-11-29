package fsm

import FsmDecl
import Program
import StateDecl
import SystemDecl

/**
 * Компиляция AST конечного автомата в FsmProgram ("байткод").
 *
 * Ограничения MVP: ожидаем один FsmDecl и один SystemDecl с одним run fsm.
 */
fun compileFsm(program: Program): FsmProgram {
    val fsm = program.decls.filterIsInstance<FsmDecl>().singleOrNull()
        ?: error("Expected exactly one FsmDecl in program")

    val system = program.decls.filterIsInstance<SystemDecl>().singleOrNull()
        ?: error("Expected exactly one SystemDecl in program")

    val run = system.runs.singleOrNull()
        ?: error("Expected exactly one run fsm statement in system")

    val fsmName = fsm.name
    if (run.fsmName != fsmName) {
        error("System runs FSM ${run.fsmName}, but FsmDecl is $fsmName")
    }

    val states = fsm.states.associateBy(
        keySelector = { it.name },
        valueTransform = { compileState(it) }
    )

    // Начальное состояние — первое объявленное
    val initialState = fsm.states.first().name

    return FsmProgram(states = states, initialState = initialState)
}

// Компилируем одно состояние автомата
private fun compileState(state: StateDecl): CompiledState {
    val onEnterInstrs = mutableListOf<Instr>()
    val transitions = mutableListOf<CompiledTransition>()

    // on enter блоки: сейчас это список строк "{heater=false;fan=true;...}"
    for (blockText in state.onEnterStatements) {
        onEnterInstrs += compileOnEnterBlock(blockText)
    }

    for (tr in state.transitions) {
        val condInstrs = compileCondition(tr.conditionText)
        transitions += CompiledTransition(
            condition = condInstrs,
            targetState = tr.targetState
        )
    }

    return CompiledState(
        name = state.name,
        onEnter = onEnterInstrs,
        transitions = transitions
    )
}

// Компилятор on enter блока.
// Поддерживаем только присваивания actuator = true/false;
private fun compileOnEnterBlock(blockText: String): List<Instr> {
    val result = mutableListOf<Instr>()

    // Убираем внешние фигурные скобки
    val inner = blockText.trim().removePrefix("{").removeSuffix("}")

    // Разбиваем по ';'
    val statements = inner.split(';')
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    for (st in statements) {
        // heater=false  или  light = true
        val parts = st.split('=').map { it.trim() }
        if (parts.size != 2) {
            error("Unsupported on-enter statement: '$st'")
        }
        val name = parts[0]
        val valueStr = parts[1]
        val boolValue = when (valueStr) {
            "true" -> true
            "false" -> false
            else -> error("Only boolean literals are supported in on-enter, got '$valueStr'")
        }

        result += PushBool(boolValue)
        result += SetActuatorBool(name)
    }

    return result
}

// Компилируем условие вида "mode==2" или "mode != 0"
private fun compileCondition(cond: String): List<Instr> {
    val text = cond.replace(" ", "")

    val op: String
    val parts: List<String>

    when {
        text.contains("==") -> {
            op = "=="
            parts = text.split("==")
        }
        text.contains("!=") -> {
            op = "!="
            parts = text.split("!=")
        }
        else -> error("Unsupported condition: '$cond'")
    }

    if (parts.size != 2) error("Malformed condition: '$cond'")

    val varName = parts[0]
    val intValue = parts[1].toIntOrNull()
        ?: error("Right-hand side must be int literal in condition '$cond'")

    val instrs = mutableListOf<Instr>()
    instrs += LoadVar(varName)
    instrs += PushInt(intValue)
    instrs += when (op) {
        "==" -> EqInt
        "!=" -> NeqInt
        else -> error("Unexpected op $op")
    }

    return instrs
}
