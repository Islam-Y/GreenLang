package org.example

import gen.GreenLangBaseVisitor
import gen.GreenLangParser
import org.antlr.v4.runtime.tree.TerminalNode
import org.example.ast.*

class AstBuilder : GreenLangBaseVisitor<Any?>() {

    fun buildProgram(ctx: GreenLangParser.ProgramContext): Program {
        val decls = ctx.topLevelDecl().map { visitTopLevelDecl(it) as TopLevelDecl }
        return Program(decls)
    }

    override fun visitTopLevelDecl(ctx: GreenLangParser.TopLevelDeclContext): Any {
        ctx.sensorDecl()?.let { return visitSensorDecl(it) }
        ctx.actuatorDecl()?.let { return visitActuatorDecl(it) }
        ctx.processDecl()?.let { return visitProcessDecl(it) }
        ctx.fsmDecl()?.let { return visitFsmDecl(it) }
        ctx.systemDecl()?.let { return visitSystemDecl(it) }

        error("Unknown topLevelDecl: ${ctx.text}")
    }

    override fun visitSensorDecl(ctx: GreenLangParser.SensorDeclContext): Any {
        val name = ctx.ID().text
        val type = visitTypeRef(ctx.typeRef())
        val sourceId = stripQuotes(ctx.STRING_LITERAL().text)
        return SensorDecl(name, type, sourceId)
    }

    override fun visitActuatorDecl(ctx: GreenLangParser.ActuatorDeclContext): Any {
        val name = ctx.ID().text
        val type = visitTypeRef(ctx.typeRef())
        val targetId = stripQuotes(ctx.STRING_LITERAL().text)
        return ActuatorDecl(name, type, targetId)
    }

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

    override fun visitProcessDecl(ctx: GreenLangParser.ProcessDeclContext): Any {
        val name = ctx.ID().text
        val params = ctx.processParams()
            ?.processParam()
            ?.map { visitProcessParam(it) }
            ?: emptyList()

        // Пока тело процесса не разбираем, просто сохраняем исходный текст блока
        val bodyText = ctx.block().text
        return ProcessDecl(name, params, body = listOf(bodyText))
    }

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

    override fun visitFsmDecl(ctx: GreenLangParser.FsmDeclContext): Any {
        val name = ctx.ID().text
        val states = ctx.stateDecl().map { visitStateDecl(it) }
        return FsmDecl(name, states)
    }

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

    override fun visitConnectStmt(ctx: GreenLangParser.ConnectStmtContext): ConnectionDecl {
        val from = visitEndpoint(ctx.endpoint(0))
        val to = visitEndpoint(ctx.endpoint(1))
        return ConnectionDecl(from, to)
    }

    override fun visitEndpoint(ctx: GreenLangParser.EndpointContext): Endpoint {
        val ids: List<TerminalNode> = ctx.ID()
        val obj = ids[0].text
        val port = if (ids.size > 1) ids[1].text else null
        return Endpoint(obj, port)
    }

    private fun stripQuotes(text: String): String =
        text.removePrefix("\"").removeSuffix("\"")
}
