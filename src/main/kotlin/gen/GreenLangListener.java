package gen;// Generated from /Users/islam/IdeaProjects/GreenLang/src/main/antlr/GreenLang.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link GreenLangParser}.
 */
public interface GreenLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(GreenLangParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(GreenLangParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#topLevelDecl}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelDecl(GreenLangParser.TopLevelDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#topLevelDecl}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelDecl(GreenLangParser.TopLevelDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#sensorDecl}.
	 * @param ctx the parse tree
	 */
	void enterSensorDecl(GreenLangParser.SensorDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#sensorDecl}.
	 * @param ctx the parse tree
	 */
	void exitSensorDecl(GreenLangParser.SensorDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#actuatorDecl}.
	 * @param ctx the parse tree
	 */
	void enterActuatorDecl(GreenLangParser.ActuatorDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#actuatorDecl}.
	 * @param ctx the parse tree
	 */
	void exitActuatorDecl(GreenLangParser.ActuatorDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeRef(GreenLangParser.TypeRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeRef(GreenLangParser.TypeRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#processDecl}.
	 * @param ctx the parse tree
	 */
	void enterProcessDecl(GreenLangParser.ProcessDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#processDecl}.
	 * @param ctx the parse tree
	 */
	void exitProcessDecl(GreenLangParser.ProcessDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#processParams}.
	 * @param ctx the parse tree
	 */
	void enterProcessParams(GreenLangParser.ProcessParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#processParams}.
	 * @param ctx the parse tree
	 */
	void exitProcessParams(GreenLangParser.ProcessParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#processParam}.
	 * @param ctx the parse tree
	 */
	void enterProcessParam(GreenLangParser.ProcessParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#processParam}.
	 * @param ctx the parse tree
	 */
	void exitProcessParam(GreenLangParser.ProcessParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(GreenLangParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(GreenLangParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(GreenLangParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(GreenLangParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#windowStmt}.
	 * @param ctx the parse tree
	 */
	void enterWindowStmt(GreenLangParser.WindowStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#windowStmt}.
	 * @param ctx the parse tree
	 */
	void exitWindowStmt(GreenLangParser.WindowStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#timeUnit}.
	 * @param ctx the parse tree
	 */
	void enterTimeUnit(GreenLangParser.TimeUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#timeUnit}.
	 * @param ctx the parse tree
	 */
	void exitTimeUnit(GreenLangParser.TimeUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#emitStmt}.
	 * @param ctx the parse tree
	 */
	void enterEmitStmt(GreenLangParser.EmitStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#emitStmt}.
	 * @param ctx the parse tree
	 */
	void exitEmitStmt(GreenLangParser.EmitStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(GreenLangParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(GreenLangParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(GreenLangParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(GreenLangParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(GreenLangParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(GreenLangParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#fsmDecl}.
	 * @param ctx the parse tree
	 */
	void enterFsmDecl(GreenLangParser.FsmDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#fsmDecl}.
	 * @param ctx the parse tree
	 */
	void exitFsmDecl(GreenLangParser.FsmDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#stateDecl}.
	 * @param ctx the parse tree
	 */
	void enterStateDecl(GreenLangParser.StateDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#stateDecl}.
	 * @param ctx the parse tree
	 */
	void exitStateDecl(GreenLangParser.StateDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#stateItem}.
	 * @param ctx the parse tree
	 */
	void enterStateItem(GreenLangParser.StateItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#stateItem}.
	 * @param ctx the parse tree
	 */
	void exitStateItem(GreenLangParser.StateItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#onEnterBlock}.
	 * @param ctx the parse tree
	 */
	void enterOnEnterBlock(GreenLangParser.OnEnterBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#onEnterBlock}.
	 * @param ctx the parse tree
	 */
	void exitOnEnterBlock(GreenLangParser.OnEnterBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#whenTransition}.
	 * @param ctx the parse tree
	 */
	void enterWhenTransition(GreenLangParser.WhenTransitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#whenTransition}.
	 * @param ctx the parse tree
	 */
	void exitWhenTransition(GreenLangParser.WhenTransitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#systemDecl}.
	 * @param ctx the parse tree
	 */
	void enterSystemDecl(GreenLangParser.SystemDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#systemDecl}.
	 * @param ctx the parse tree
	 */
	void exitSystemDecl(GreenLangParser.SystemDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#systemItem}.
	 * @param ctx the parse tree
	 */
	void enterSystemItem(GreenLangParser.SystemItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#systemItem}.
	 * @param ctx the parse tree
	 */
	void exitSystemItem(GreenLangParser.SystemItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#connectStmt}.
	 * @param ctx the parse tree
	 */
	void enterConnectStmt(GreenLangParser.ConnectStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#connectStmt}.
	 * @param ctx the parse tree
	 */
	void exitConnectStmt(GreenLangParser.ConnectStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint(GreenLangParser.EndpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint(GreenLangParser.EndpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#runStmt}.
	 * @param ctx the parse tree
	 */
	void enterRunStmt(GreenLangParser.RunStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#runStmt}.
	 * @param ctx the parse tree
	 */
	void exitRunStmt(GreenLangParser.RunStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDivExpr(GreenLangParser.MulDivExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDivExpr(GreenLangParser.MulDivExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRelExpr(GreenLangParser.RelExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRelExpr(GreenLangParser.RelExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicExpr(GreenLangParser.LogicExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicExpr(GreenLangParser.LogicExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqExpr(GreenLangParser.EqExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqExpr(GreenLangParser.EqExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(GreenLangParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(GreenLangParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(GreenLangParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(GreenLangParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(GreenLangParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(GreenLangParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSubExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSubExpr(GreenLangParser.AddSubExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSubExpr}
	 * labeled alternative in {@link GreenLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSubExpr(GreenLangParser.AddSubExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(GreenLangParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(GreenLangParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link GreenLangParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(GreenLangParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link GreenLangParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(GreenLangParser.PrimaryContext ctx);
}