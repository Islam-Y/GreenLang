import gen.GreenLangLexer
import gen.GreenLangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import fsm.FloatVal
import fsm.IntVal
import fsm.compileFsm
import process.ProcessCompiler
import org.example.process.ProcessVm

/**
 * Демонстрация: парсим example.green -> AST -> компилируем FSM -> гоняем VM.
 *
 * Ограничения: поддерживается только подмножество конструкций FSM, используемых в example.green.
 */
fun main(args: Array<String>) {
    val fileName = if (args.isNotEmpty()) args[0] else "src/main/resources/example.green"
    println("Parsing file: $fileName")

    val input = CharStreams.fromFileName(fileName)
    val lexer = GreenLangLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = GreenLangParser(tokens)

    val tree = parser.program()

    // Строим AST
    val builder = AstBuilder()
    val programAst: Program = builder.buildProgram(tree as GreenLangParser.ProgramContext)

    // Компилируем FSM в байткод
    val fsmProgram = compileFsm(programAst)

    // Создаём VM для FSM
    val fsmVm = FsmVm(fsmProgram)

    // Компилируем контроллер температуры в процессный байткод
    val tempController = programAst.decls.filterIsInstance<ProcessDecl>()
        .find { it.name == "TempController" }
        ?: error("TempController process not found")
    val tempControllerProgram = ProcessCompiler.compile(tempController)
    val tempControllerVm = ProcessVm(tempControllerProgram)

    println("TempController bytecode:")
    tempControllerProgram.instrs.forEachIndexed { idx, instr ->
        println("  $idx: $instr")
    }

    // Прогоняем несколько шагов: t -> TempController -> mode -> FSM
    println("=== FSM run driven by TempController ===")
    val tempValues = listOf(25.0, 29.0, 18.0, 23.0)

    for (t in tempValues) {
        val outputs = tempControllerVm.run(mapOf("t" to FloatVal(t)))
        val modeVal = outputs["mode"] as? IntVal
            ?: error("TempController did not emit mode")
        println("Input t=$t -> mode=${modeVal.value}")
        fsmVm.setIntVar("mode", modeVal.value)
        fsmVm.step()
    }
}
