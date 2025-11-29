import fsm.AddFloat
import fsm.BoolVal
import fsm.DivFloat
import fsm.EmitValue
import fsm.EqFloat
import fsm.EqInt
import fsm.FsmProgram
import fsm.GeFloat
import fsm.GtFloat
import fsm.Instr
import fsm.IntVal
import fsm.Jump
import fsm.JumpIfFalse
import fsm.LeFloat
import fsm.LoadVar
import fsm.LtFloat
import fsm.MulFloat
import fsm.NeqFloat
import fsm.NeqInt
import fsm.NoOp
import fsm.PushBool
import fsm.PushFloat
import fsm.PushInt
import fsm.SetActuatorBool
import fsm.StoreVar
import fsm.SubFloat
import fsm.Value

/**
 * Мини-VM для исполнения FSM-биткода.
 *
 * Поддерживает:
 *  - хранение текущего состояния;
 *  - onEnter: выполняет последовательность Instr;
 *  - переходы: проверяет условия по очереди, при срабатывании — меняет state и выполняет onEnter нового state.
 */
class FsmVm(
    private val program: FsmProgram
) {
    // окружение переменных, в примере: "mode" : IntVal
    val env: MutableMap<String, Value> = mutableMapOf()

    // актуаторы: heater / fan / light
    val actuators: MutableMap<String, Boolean> = mutableMapOf()

    private var currentStateName: String = program.initialState
    private var firstEnter: Boolean = true

    // простой стек для исполнения инструкций
    private val stack = ArrayDeque<Value>()

    fun currentState(): String = currentStateName

    // Установить значение переменной (например, mode)
    fun setIntVar(name: String, value: Int) {
        env[name] = IntVal(value)
    }

    /**
     * Одно "шаговое" обновление автомата:
     * 1) если только вошли в состояние — выполняем onEnter
     * 2) проверяем переходы, если переход сработал — меняем состояние и снова делаем onEnter нового
     */
    fun step() {
        if (firstEnter) {
            println("Entering initial state: $currentStateName")
            executeOnEnter(currentStateName)
            firstEnter = false
        }

        val state = program.states[currentStateName]
            ?: error("Unknown state '$currentStateName'")

        for (tr in state.transitions) {
            val condValue = evalCondition(tr.condition)
            if (condValue) {
                println("Transition: $currentStateName --[cond]--> ${tr.targetState}")
                currentStateName = tr.targetState
                executeOnEnter(currentStateName)
                return
            }
        }

        println("No transition fired from state $currentStateName")
    }

    private fun executeOnEnter(stateName: String) {
        val state = program.states[stateName]
            ?: error("Unknown state '$stateName'")

        println("onEnter($stateName)")

        executeInstrs(state.onEnter)

        println("Actuators:")
        actuators.forEach { (name, value) ->
            println("  $name = $value")
        }
    }

    private fun evalCondition(instrs: List<Instr>): Boolean {
        stack.clear()
        executeInstrs(instrs)
        val top = stack.removeLastOrNull()
            ?: error("Condition left empty stack")

        return when (top) {
            is BoolVal -> top.value
            else -> error("Condition must produce Bool, got $top")
        }
    }

    private fun executeInstrs(instrs: List<Instr>) {
        for (ins in instrs) {
            when (ins) {
                is PushInt -> stack.addLast(IntVal(ins.value))
                is PushBool -> stack.addLast(BoolVal(ins.value))

                is LoadVar -> {
                    val v = env[ins.name]
                        ?: error("Variable '${ins.name}' is not set in env")
                    stack.addLast(v)
                }

                is StoreVar -> {
                    val v = stack.removeLastOrNull()
                        ?: error("STORE_VAR on empty stack")
                    env[ins.name] = v
                }

                EqInt -> {
                    val b = stack.removeLastOrNull() as? IntVal
                        ?: error("EQ_INT expects Int on stack (b)")
                    val a = stack.removeLastOrNull() as? IntVal
                        ?: error("EQ_INT expects Int on stack (a)")
                    stack.addLast(BoolVal(a.value == b.value))
                }

                NeqInt -> {
                    val b = stack.removeLastOrNull() as? IntVal
                        ?: error("NEQ_INT expects Int on stack (b)")
                    val a = stack.removeLastOrNull() as? IntVal
                        ?: error("NEQ_INT expects Int on stack (a)")
                    stack.addLast(BoolVal(a.value != b.value))
                }

                is SetActuatorBool -> {
                    val v = stack.removeLastOrNull() as? BoolVal
                        ?: error("SetActuatorBool expects Bool on stack")
                    actuators[ins.name] = v.value
                }

                NoOp -> {
                    // ничего не делаем
                }

                // Инструкции, не используемые в FSM-VM (при появлении — ошибка)
                is PushFloat,
                is EqFloat,
                is NeqFloat,
                is GtFloat,
                is LtFloat,
                is GeFloat,
                is LeFloat,
                is AddFloat,
                is SubFloat,
                is MulFloat,
                is DivFloat,
                is Jump,
                is JumpIfFalse,
                is EmitValue -> error("Instruction ${ins.javaClass.simpleName} is not supported in FSM VM")
            }
        }
    }
}
