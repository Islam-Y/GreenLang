package process

import Assign
import BinOp
import BoolLit
import BoolType
import Emit
import Expr
import FloatLit
import FloatType
import IfStmt
import IntLit
import IntType
import NoOpStmt
import ProcessDecl
import ProcessParamDir
import Stmt
import StreamType
import TypeRef
import VarRef
import fsm.AddFloat
import fsm.DivFloat
import fsm.EmitValue
import fsm.EqFloat
import fsm.EqInt
import fsm.GeFloat
import fsm.GtFloat
import fsm.Instr
import fsm.Jump
import fsm.JumpIfFalse
import fsm.LeFloat
import fsm.LoadVar
import fsm.LtFloat
import fsm.MulFloat
import fsm.NeqFloat
import fsm.NeqInt
import fsm.PushBool
import fsm.PushFloat
import fsm.PushInt
import fsm.StoreVar
import fsm.SubFloat
import org.example.process.ProcessProgram

/**
 * Компилятор процессов: ProcessDecl -> ProcessProgram (список Instr).
 * Набор операторов/выражений, достаточный для TempController.
 */
object ProcessCompiler {
    /**
     * Компилирует процесс в байткод ProcessProgram, чтобы его исполнила ProcessVm.
     */
    fun compile(process: ProcessDecl): ProcessProgram {
        val inputs = process.params.filter { it.direction == ProcessParamDir.IN }
            .associate { it.name to unwrapStream(it.type) }
        val outputs = process.params.filter { it.direction == ProcessParamDir.OUT }
            .associate { it.name to unwrapStream(it.type) }

        val symbols: Map<String, TypeRef> = mutableMapOf<String, TypeRef>().apply {
            putAll(inputs)
            putAll(outputs)
        }

        val instrs = mutableListOf<Instr>()
        for (stmt in process.body) {
            val base = instrs.size
            val raw = compileStmt(stmt, symbols, outputs.keys)
            instrs += shiftJumps(raw, base)
        }

        return ProcessProgram(
            name = process.name,
            inputs = inputs,
            outputs = outputs,
            instrs = instrs
        )
    }

    /**
     * Разворачивает stream<T> в T, чтобы оперировать типами значений.
     */
    private fun unwrapStream(type: TypeRef): TypeRef =
        if (type is StreamType) type.elementType else type

    /**
     * Преобразует оператор в список инструкций с учётом таблицы символов.
     */
    private fun compileStmt(stmt: Stmt, symbols: Map<String, TypeRef>, outputs: Set<String>): List<Instr> =
        when (stmt) {
            is Assign -> {
                val (code, _) = compileExpr(stmt.expr, symbols)
                code + StoreVar(stmt.name)
            }

            is Emit -> {
                val (code) = compileExpr(stmt.expr, symbols)
                val targetName = when (stmt.expr) {
                    is VarRef -> stmt.expr.name
                    else -> outputs.singleOrNull()
                        ?: error("Emit expects VarRef or single output param")
                }
                if (targetName !in outputs) {
                    error("Emit target '$targetName' is not declared as output")
                }
                code + EmitValue(targetName)
            }

            is IfStmt -> compileIf(stmt, symbols, outputs)
            NoOpStmt -> emptyList()
        }

    /**
     * Компилирует if-ветвление, расставляя Jump/JumpIfFalse.
     */
    private fun compileIf(stmt: IfStmt, symbols: Map<String, TypeRef>, outputs: Set<String>): List<Instr> {
        val code = mutableListOf<Instr>()
        val (condCode, _) = compileExpr(stmt.condition, symbols)
        code += condCode
        val jumpIfFalsePos = code.size
        code += JumpIfFalse(-1)

        val thenCode = stmt.thenBranch.flatMap { compileStmt(it, symbols, outputs) }
        code += shiftJumps(thenCode, code.size)
        val jumpPos = code.size
        code += Jump(-1)

        val elseStart = code.size
        code[jumpIfFalsePos] = JumpIfFalse(elseStart)

        val elseCode = stmt.elseBranch?.flatMap { compileStmt(it, symbols, outputs) } ?: emptyList()
        code += shiftJumps(elseCode, code.size)

        val end = code.size
        code[jumpPos] = Jump(end)

        return code
    }

    private data class ExprResult(val code: List<Instr>, val type: TypeRef)

    /**
     * Компилирует выражение в инструкции и вычисленный тип результата.
     */
    private fun compileExpr(expr: Expr, symbols: Map<String, TypeRef>): ExprResult =
        when (expr) {
            is VarRef -> {
                val type = symbols[expr.name] ?: error("Unknown var ${expr.name}")
                ExprResult(listOf(LoadVar(expr.name)), type)
            }

            is IntLit -> ExprResult(listOf(PushInt(expr.value)), IntType)
            is FloatLit -> ExprResult(listOf(PushFloat(expr.value)), FloatType)
            is BoolLit -> ExprResult(listOf(PushBool(expr.value)), BoolType)

            is BinOp -> compileBinOp(expr, symbols)
        }

    /**
     * Компилирует бинарную операцию, выбирая инструкцию по оператору и типам.
     */
    private fun compileBinOp(bin: BinOp, symbols: Map<String, TypeRef>): ExprResult {
        val left = compileExpr(bin.left, symbols)
        val right = compileExpr(bin.right, symbols)
        val op = bin.op

        // Для простоты считаем, что числа — float, сравнения по float; eq/neq по типу операндов.
        val code = mutableListOf<Instr>()
        code += left.code
        code += right.code

        val type = left.type
        return when (op) {
            "+" -> ExprResult(code + AddFloat, type)
            "-" -> ExprResult(code + SubFloat, type)
            "*" -> ExprResult(code + MulFloat, type)
            "/" -> ExprResult(code + DivFloat, type)
            ">" -> ExprResult(code + GtFloat, BoolType)
            "<" -> ExprResult(code + LtFloat, BoolType)
            ">=" -> ExprResult(code + GeFloat, BoolType)
            "<=" -> ExprResult(code + LeFloat, BoolType)
            "==" -> {
                val instr = when (type) {
                    IntType -> EqInt
                    FloatType -> EqFloat
                    BoolType -> EqFloat // bool eq не нужен для MVP
                    else -> EqFloat
                }
                ExprResult(code + instr, BoolType)
            }
            "!=" -> {
                val instr = when (type) {
                    IntType -> NeqInt
                    FloatType -> NeqFloat
                    BoolType -> NeqFloat
                    else -> NeqFloat
                }
                ExprResult(code + instr, BoolType)
            }
            else -> error("Unsupported operator '$op'")
        }
    }

    /**
     * Сдвигает относительные адреса переходов на заданный offset.
     */
    private fun shiftJumps(code: List<Instr>, offset: Int): List<Instr> =
        code.map {
            when (it) {
                is Jump -> it.copy(target = it.target + offset)
                is JumpIfFalse -> it.copy(target = it.target + offset)
                else -> it
            }
        }
}
