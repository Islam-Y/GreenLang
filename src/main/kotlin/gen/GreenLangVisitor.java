package gen;// Generated from /Users/islam/IdeaProjects/GreenLang/src/main/antlr/GreenLang.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GreenLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GreenLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(GreenLangParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#topLevelDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTopLevelDecl(GreenLangParser.TopLevelDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#sensorDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSensorDecl(GreenLangParser.SensorDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#actuatorDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActuatorDecl(GreenLangParser.ActuatorDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#typeRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRef(GreenLangParser.TypeRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#processDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessDecl(GreenLangParser.ProcessDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#processParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessParams(GreenLangParser.ProcessParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#processParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcessParam(GreenLangParser.ProcessParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(GreenLangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(GreenLangParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#windowStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowStmt(GreenLangParser.WindowStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#timeUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeUnit(GreenLangParser.TimeUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#emitStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmitStmt(GreenLangParser.EmitStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(GreenLangParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(GreenLangParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(GreenLangParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#fsmDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsmDecl(GreenLangParser.FsmDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#stateDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateDecl(GreenLangParser.StateDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#stateItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateItem(GreenLangParser.StateItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#onEnterBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnEnterBlock(GreenLangParser.OnEnterBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#whenTransition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenTransition(GreenLangParser.WhenTransitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#systemDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemDecl(GreenLangParser.SystemDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#systemItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemItem(GreenLangParser.SystemItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#connectStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnectStmt(GreenLangParser.ConnectStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#endpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpoint(GreenLangParser.EndpointContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#runStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRunStmt(GreenLangParser.RunStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpr(GreenLangParser.MulDivExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(GreenLangParser.RelExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicExpr(GreenLangParser.LogicExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(GreenLangParser.EqExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(GreenLangParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(GreenLangParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(GreenLangParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSubExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpr(GreenLangParser.AddSubExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(GreenLangParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link GreenLangParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(GreenLangParser.PrimaryContext ctx);
}