// src/main/kotlin/Main.kt
package org.example

import gen.GreenLangLexer
import gen.GreenLangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.example.ast.Program

fun main(args: Array<String>) {
    // Если путь к файлу передали аргументом — берём его,
    // иначе — дефолтный example.green в ресурсах/рядом с проектом.
    val fileName = if (args.isNotEmpty()) args[0] else "src/main/resources/example.green"

    println("Parsing file: $fileName")

    // 1. Читаем исходник
    val input = CharStreams.fromFileName(fileName)

    // 2. Лексер
    val lexer = GreenLangLexer(input)

    // 3. Поток токенов
    val tokens = CommonTokenStream(lexer)

    // 4. Парсер
    val parser = GreenLangParser(tokens)

    // 5. Стартовое правило грамматики
    val tree: ParseTree = parser.program()

    // 6. Строим AST и выводим
    val builder = AstBuilder()
    val programAst: Program = builder.buildProgram(tree as GreenLangParser.ProgramContext)

    println("=== AST ===")
    println(programAst)
}

private fun prettyTree(tree: ParseTree, parser: GreenLangParser): String {
    val sb = StringBuilder()
    // корень без соединителя, дальше — ветки с псевдографикой
    sb.append(tree.textFor(parser)).append('\n')
    fun visit(node: ParseTree, prefix: String, isTail: Boolean) {
        val connector = if (isTail) "└── " else "├── "
        sb.append(prefix).append(connector).append(node.textFor(parser)).append('\n')

        val childCount = node.childCount
        for (i in 0 until childCount) {
            val child = node.getChild(i)
            val nextPrefix = prefix + if (isTail) "    " else "│   "
            visit(child, nextPrefix, i == childCount - 1)
        }
    }
    for (i in 0 until tree.childCount) {
        visit(tree.getChild(i), "", i == tree.childCount - 1)
    }
    return sb.toString().trimEnd()
}

private fun ParseTree.textFor(parser: GreenLangParser): String =
    when (this) {
        is org.antlr.v4.runtime.tree.TerminalNode -> this.text
        else -> parser.ruleNames[(this as org.antlr.v4.runtime.RuleContext).ruleIndex]
    }
