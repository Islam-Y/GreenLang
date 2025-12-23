/** AST узлы GreenLang. */

/** Маркер верхнеуровневых объявлений. */
sealed interface TopLevelDecl

/** Корневой узел программы. */
data class Program(
    /** Верхнеуровневые объявления в порядке следования. */
    val decls: List<TopLevelDecl>
)

/** Объявление сенсора. */
data class SensorDecl(
    /** Имя сенсора. */
    val name: String,
    /** Тип сенсора. */
    val type: TypeRef,
    /** Идентификатор источника данных. */
    val sourceId: String
) : TopLevelDecl

/** Объявление актуатора. */
data class ActuatorDecl(
    /** Имя актуатора. */
    val name: String,
    /** Тип актуатора. */
    val type: TypeRef,
    /** Идентификатор целевого устройства. */
    val targetId: String
) : TopLevelDecl

/** Ссылка на тип. */
sealed interface TypeRef

/** Тип int. */
data object IntType : TypeRef

/** Тип float. */
data object FloatType : TypeRef

/** Тип bool. */
data object BoolType : TypeRef

/** Тип string. */
data object StringType : TypeRef

/** Тип stream<T>. */
data class StreamType(val elementType: TypeRef) : TypeRef

/** Объявление процесса. */
data class ProcessDecl(
    /** Имя процесса. */
    val name: String,
    /** Параметры процесса. */
    val params: List<ProcessParam>,
    /** Тело процесса. */
    val body: List<Stmt>
) : TopLevelDecl

/** Параметр процесса. */
data class ProcessParam(
    /** Направление потока. */
    val direction: ProcessParamDir,
    /** Имя параметра. */
    val name: String,
    /** Тип параметра. */
    val type: TypeRef
)

/** Направление параметра процесса. */
enum class ProcessParamDir { IN, OUT }

/** Объявление конечного автомата. */
data class FsmDecl(
    /** Имя автомата. */
    val name: String,
    /** Состояния автомата. */
    val states: List<StateDecl>
) : TopLevelDecl

/** Объявление состояния. */
data class StateDecl(
    /** Имя состояния. */
    val name: String,
    /** Операторы блока on enter (хранятся как текст). */
    val onEnterStatements: List<String>,
    /** Переходы из состояния. */
    val transitions: List<TransitionDecl>
)

/** Объявление перехода. */
data class TransitionDecl(
    /** Условие перехода (в текстовом виде). */
    val conditionText: String,
    /** Имя целевого состояния. */
    val targetState: String
)

/** Объявление системы. */
data class SystemDecl(
    /** Имя системы. */
    val name: String,
    /** Соединения между компонентами. */
    val connections: List<ConnectionDecl>,
    /** Запуски FSM. */
    val runs: List<RunFsmDecl>
) : TopLevelDecl

/** Соединение endpoint'ов. */
data class ConnectionDecl(
    /** Исходный endpoint. */
    val from: Endpoint,
    /** Целевой endpoint. */
    val to: Endpoint
)

/** Ссылка на endpoint: объект и (опционально) порт. */
data class Endpoint(
    /** Имя объекта. */
    val objectName: String,
    /** Имя порта (если задано). */
    val portName: String?
)

/** Запуск конечного автомата. */
data class RunFsmDecl(
    /** Имя FSM для запуска. */
    val fsmName: String
) : TopLevelDecl

// -------------------------
// Императивные выражения/операторы (минимальный набор)
// -------------------------

/** Базовый тип выражений. */
sealed interface Expr

/** Ссылка на переменную. */
data class VarRef(val name: String) : Expr

/** Целочисленный литерал. */
data class IntLit(val value: Int) : Expr

/** Литерал с плавающей точкой. */
data class FloatLit(val value: Double) : Expr

/** Булев литерал. */
data class BoolLit(val value: Boolean) : Expr

/** Бинарная операция. */
data class BinOp(val left: Expr, val op: String, val right: Expr) : Expr

/** Базовый тип операторов. */
sealed interface Stmt

/** Присваивание. */
data class Assign(val name: String, val expr: Expr) : Stmt

/** Отправка значения в поток. */
data class Emit(val expr: Expr) : Stmt

/** Условный оператор. */
data class IfStmt(val condition: Expr, val thenBranch: List<Stmt>, val elseBranch: List<Stmt>?) : Stmt

/** Пустой оператор. */
data object NoOpStmt : Stmt
