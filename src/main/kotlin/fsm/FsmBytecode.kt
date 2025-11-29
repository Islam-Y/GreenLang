package fsm

/**
 * Мини-байткод для FSM/процессов: значения, инструкции и скомпилированные состояния.
 */
// Значения в окружении VM
sealed interface Value
data class IntVal(val value: Int) : Value
data class FloatVal(val value: Double) : Value
data class BoolVal(val value: Boolean) : Value

// Примитивные инструкции "байткода" для FSM
sealed interface Instr

data class PushInt(val value: Int) : Instr
data class PushFloat(val value: Double) : Instr
data class PushBool(val value: Boolean) : Instr
data class LoadVar(val name: String) : Instr
data class StoreVar(val name: String) : Instr
data object EqInt : Instr          // сравнение двух Int на стеке -> Bool
data object NeqInt : Instr
data object EqFloat : Instr
data object NeqFloat : Instr
data object GtFloat : Instr
data object LtFloat : Instr
data object GeFloat : Instr
data object LeFloat : Instr
data object AddFloat : Instr
data object SubFloat : Instr
data object MulFloat : Instr
data object DivFloat : Instr
data class Jump(val target: Int) : Instr
data class JumpIfFalse(val target: Int) : Instr
data class EmitValue(val name: String) : Instr
data class SetActuatorBool(val name: String) : Instr  // берёт Bool со стека и пишет в актюатор
data object NoOp : Instr

// Транзиции и состояние автомата
data class CompiledTransition(
    val condition: List<Instr>,
    val targetState: String
)

data class CompiledState(
    val name: String,
    val onEnter: List<Instr>,
    val transitions: List<CompiledTransition>
)

data class FsmProgram(
    val states: Map<String, CompiledState>,
    val initialState: String
)
