sealed interface TopLevelDecl

data class Program(
    val decls: List<TopLevelDecl>
)

// sensor temp: float from "T1";
data class SensorDecl(
    val name: String,
    val type: TypeRef,
    val sourceId: String
) : TopLevelDecl

// actuator heater: bool to "HEAT1";
data class ActuatorDecl(
    val name: String,
    val type: TypeRef,
    val targetId: String
) : TopLevelDecl

// типы
sealed interface TypeRef
data object IntType : TypeRef
data object FloatType : TypeRef
data object BoolType : TypeRef
data object StringType : TypeRef
data class StreamType(val elementType: TypeRef) : TypeRef

// process ... { ... }
data class ProcessDecl(
    val name: String,
    val params: List<ProcessParam>,
    val body: List<Stmt>
) : TopLevelDecl

data class ProcessParam(
    val direction: ProcessParamDir,
    val name: String,
    val type: TypeRef
)

enum class ProcessParamDir { IN, OUT }

// FSM
data class FsmDecl(
    val name: String,
    val states: List<StateDecl>
) : TopLevelDecl

data class StateDecl(
    val name: String,
    val onEnterStatements: List<String>,
    val transitions: List<TransitionDecl>
)

data class TransitionDecl(
    val conditionText: String,
    val targetState: String
)

// system
data class SystemDecl(
    val name: String,
    val connections: List<ConnectionDecl>,
    val runs: List<RunFsmDecl>
) : TopLevelDecl

data class ConnectionDecl(
    val from: Endpoint,
    val to: Endpoint
)

data class Endpoint(
    val objectName: String,
    val portName: String?
)

data class RunFsmDecl(
    val fsmName: String
) : TopLevelDecl

// -------------------------
// Императивные выражения/операторы (минимальный набор)
// -------------------------

sealed interface Expr
data class VarRef(val name: String) : Expr
data class IntLit(val value: Int) : Expr
data class FloatLit(val value: Double) : Expr
data class BoolLit(val value: Boolean) : Expr
data class BinOp(val left: Expr, val op: String, val right: Expr) : Expr

sealed interface Stmt
data class Assign(val name: String, val expr: Expr) : Stmt
data class Emit(val expr: Expr) : Stmt
data class IfStmt(val condition: Expr, val thenBranch: List<Stmt>, val elseBranch: List<Stmt>?) : Stmt
data object NoOpStmt : Stmt
