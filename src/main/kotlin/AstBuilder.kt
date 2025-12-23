import gen.GreenLangBaseVisitor
import gen.GreenLangParser
import org.antlr.v4.runtime.tree.TerminalNode

/**
 * Строит независимый AST из parse tree ANTLR.
 * Покрывает верхнеуровневые декларации, минимальные выражения и стейтменты (assign/emit/if).
 */
class AstBuilder : GreenLangBaseVisitor<Any?>() {

    /**
     * Собирает корневой AST из контекста программы, чтобы получить список объявлений.
     */
    fun buildProgram(ctx: GreenLangParser.ProgramContext): Program {
        val decls = ctx.topLevelDecl().map { visitTopLevelDecl(it) as TopLevelDecl }
        return Program(decls)
    }

    /**
     * Делегирует обработку верхнеуровневого правила на конкретный visitor.
     */
    override fun visitTopLevelDecl(ctx: GreenLangParser.TopLevelDeclContext): Any {
        ctx.sensorDecl()?.let { return visitSensorDecl(it) }
        ctx.actuatorDecl()?.let { return visitActuatorDecl(it) }
        ctx.processDecl()?.let { return visitProcessDecl(it) }
        ctx.fsmDecl()?.let { return visitFsmDecl(it) }
        ctx.systemDecl()?.let { return visitSystemDecl(it) }

        error("Unknown topLevelDecl: ${ctx.text}")
    }

    /**
     * Строит AST-узел сенсора из парсерного контекста.
     */
    override fun visitSensorDecl(ctx: GreenLangParser.SensorDeclContext): Any {
        val name = ctx.ID().text
        val type = visitTypeRef(ctx.typeRef())
        val sourceId = stripQuotes(ctx.STRING_LITERAL().text)
        return SensorDecl(name, type, sourceId)
    }

    /**
     * Строит AST-узел актуатора из парсерного контекста.
     */
    override fun visitActuatorDecl(ctx: GreenLangParser.ActuatorDeclContext): Any {
        val name = ctx.ID().text
        val type = visitTypeRef(ctx.typeRef())
        val targetId = stripQuotes(ctx.STRING_LITERAL().text)
        return ActuatorDecl(name, type, targetId)
    }

    /**
     * Преобразует грамматический typeRef в соответствующий AST TypeRef.
     */
    override fun visitTypeRef(ctx: GreenLangParser.TypeRefContext): TypeRef {
        // typeRef:
        //   'int' | 'float' | 'bool' | 'string' | 'stream' '<' typeRef '>'
        return when (ctx.start.text) {
            "int" -> IntType
            "float" -> FloatType
            "bool" -> BoolType
            "string" -> StringType
            "stream" -> StreamType(visitTypeRef(ctx.typeRef(0)))
            else -> error("Unknown typeRef: ${ctx.text}")
        }
    }

    /**
     * Строит процесс с параметрами и телом из парсерного контекста.
     */
    override fun visitProcessDecl(ctx: GreenLangParser.ProcessDeclContext): Any {
        val name = ctx.ID().text
        val params = ctx.processParams()
            ?.processParam()
            ?.map { visitProcessParam(it) }
            ?: emptyList()

        val body = buildBlock(ctx.block())
        return ProcessDecl(name, params, body = body)
    }

    /**
     * Преобразует параметр процесса, включая направление и тип потока.
     */
    override fun visitProcessParam(ctx: GreenLangParser.ProcessParamContext): ProcessParam {
        val dir = when (ctx.getChild(0).text) {
            "in" -> ProcessParamDir.IN
            "out" -> ProcessParamDir.OUT
            else -> error("Unknown direction: ${ctx.getChild(0).text}")
        }
        val name = ctx.ID().text
        // grammar: 'stream' '<' typeRef '>' — оборачиваем inner в StreamType
        val elementType = visitTypeRef(ctx.typeRef())
        return ProcessParam(dir, name, StreamType(elementType))
    }

    // -------------------------
    // Стейтменты и выражения
    // -------------------------

    /**
     * Разворачивает блок в список операторов, фильтруя пустые.
     */
    private fun buildBlock(blockCtx: GreenLangParser.BlockContext): List<Stmt> =
        blockCtx.statement().mapNotNull { buildStmt(it) }

    /**
     * Преобразует statement в AST-оператор, игнорируя неподдерживаемые.
     */
    private fun buildStmt(ctx: GreenLangParser.StatementContext): Stmt? {
        ctx.windowStmt()?.let { return NoOpStmt } // игнорируем окно в MVP
        ctx.emitStmt()?.let { return Emit(buildExpr(it.expr())) }
        ctx.assignment()?.let {
            val name = it.ID().text
            return Assign(name, buildExpr(it.expr()))
        }
        ctx.ifStmt()?.let { return buildIfStmt(it) }
        ctx.exprStmt()?.let { return NoOpStmt } // не поддерживаем отдельные выражения
        // пустая строка ";" — null
        return null
    }

    /**
     * Строит IfStmt с then/else ветками.
     */
    private fun buildIfStmt(ctx: GreenLangParser.IfStmtContext): IfStmt {
        val cond = buildExpr(ctx.expr())
        val thenBranch = buildBlock(ctx.block(0))
        val elseBranch = ctx.block(1)?.let { buildBlock(it) }
        return IfStmt(cond, thenBranch, elseBranch)
    }

    /**
     * Компилирует выражение в AST, сохраняя структуру и приоритеты.
     */
    private fun buildExpr(ctx: GreenLangParser.ExprContext): Expr {
        return when (ctx) {
            is GreenLangParser.AddSubExprContext -> BinOp(buildExpr(ctx.expr(0)), ctx.op.text, buildExpr(ctx.expr(1)))
            is GreenLangParser.MulDivExprContext -> BinOp(buildExpr(ctx.expr(0)), ctx.op.text, buildExpr(ctx.expr(1)))
            is GreenLangParser.RelExprContext -> BinOp(buildExpr(ctx.expr(0)), ctx.op.text, buildExpr(ctx.expr(1)))
            is GreenLangParser.EqExprContext -> BinOp(buildExpr(ctx.expr(0)), ctx.op.text, buildExpr(ctx.expr(1)))
            is GreenLangParser.LogicExprContext -> BinOp(buildExpr(ctx.expr(0)), ctx.op.text, buildExpr(ctx.expr(1)))
            is GreenLangParser.ParenExprContext -> buildExpr(ctx.expr())
            is GreenLangParser.CallExprContext -> {
                // Для MVP: считаем вызов VarRef по имени функции, аргументы игнорируем
                VarRef(ctx.functionCall().ID().text)
            }
            is GreenLangParser.PrimaryExprContext -> buildPrimary(ctx.primary())
            else -> error("Unsupported expr: ${ctx.text}")
        }
    }

    /**
     * Преобразует primary в литерал или ссылку на переменную.
     */
    private fun buildPrimary(ctx: GreenLangParser.PrimaryContext): Expr {
        ctx.INT_LITERAL()?.let { return IntLit(it.text.toInt()) }
        ctx.FLOAT_LITERAL()?.let { return FloatLit(it.text.toDouble()) }
        ctx.BOOL_LITERAL()?.let { return BoolLit(it.text.toBooleanStrict()) }
        ctx.ID()?.let { return VarRef(it.text) }
        error("Unsupported primary: ${ctx.text}")
    }

    /**
     * Строит описание конечного автомата с его состояниями.
     */
    override fun visitFsmDecl(ctx: GreenLangParser.FsmDeclContext): Any {
        val name = ctx.ID().text
        val states = ctx.stateDecl().map { visitStateDecl(it) }
        return FsmDecl(name, states)
    }

    /**
     * Собирает состояние FSM, выделяя onEnter и переходы.
     */
    override fun visitStateDecl(ctx: GreenLangParser.StateDeclContext): StateDecl {
        val name = ctx.ID().text
        val onEnterBlocks = mutableListOf<String>()
        val transitions = mutableListOf<TransitionDecl>()

        for (item in ctx.stateItem()) {
            item.onEnterBlock()?.let {
                onEnterBlocks.add(it.block().text)
            }
            item.whenTransition()?.let {
                val cond = it.expr().text
                val target = it.ID().text
                transitions.add(TransitionDecl(cond, target))
            }
        }

        return StateDecl(name, onEnterBlocks, transitions)
    }

    /**
     * Собирает системные объявления connect/run в SystemDecl.
     */
    override fun visitSystemDecl(ctx: GreenLangParser.SystemDeclContext): Any {
        val name = ctx.ID().text
        val connections = mutableListOf<ConnectionDecl>()
        val runs = mutableListOf<RunFsmDecl>()

        for (item in ctx.systemItem()) {
            item.connectStmt()?.let {
                connections.add(visitConnectStmt(it))
            }
            item.runStmt()?.let {
                runs.add(RunFsmDecl(it.ID().text))
            }
        }

        return SystemDecl(name, connections, runs)
    }

    /**
     * Преобразует connect в ConnectionDecl.
     */
    override fun visitConnectStmt(ctx: GreenLangParser.ConnectStmtContext): ConnectionDecl {
        val from = visitEndpoint(ctx.endpoint(0))
        val to = visitEndpoint(ctx.endpoint(1))
        return ConnectionDecl(from, to)
    }

    /**
     * Строит endpoint из пары ID[.ID] в виде объекта и опционального порта.
     */
    override fun visitEndpoint(ctx: GreenLangParser.EndpointContext): Endpoint {
        val ids: List<TerminalNode> = ctx.ID()
        val obj = ids[0].text
        val port = if (ids.size > 1) ids[1].text else null
        return Endpoint(obj, port)
    }

    /**
     * Удаляет внешние кавычки у строкового литерала.
     */
    private fun stripQuotes(text: String): String =
        text.removePrefix("\"").removeSuffix("\"")
}
