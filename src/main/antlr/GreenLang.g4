grammar GreenLang;

// -------------------------
// Parser rules
// -------------------------

program
    : topLevelDecl* EOF
    ;

topLevelDecl
    : sensorDecl
    | actuatorDecl
    | processDecl
    | fsmDecl
    | systemDecl
    ;

sensorDecl
    : 'sensor' ID ':' typeRef 'from' STRING_LITERAL ';'
    ;

actuatorDecl
    : 'actuator' ID ':' typeRef 'to' STRING_LITERAL ';'
    ;

// типы: примитивы + stream<T>
typeRef
    : 'int'
    | 'float'
    | 'bool'
    | 'string'
    | 'stream' '<' typeRef '>'
    ;

processDecl
    : 'process' ID '(' processParams? ')' block
    ;

processParams
    : processParam (',' processParam)*
    ;

processParam
    : ('in' | 'out') ID ':' 'stream' '<' typeRef '>'
    ;

block
    : '{' statement* '}'
    ;

statement
    : windowStmt
    | emitStmt
    | assignment
    | ifStmt
    | exprStmt
    | ';'                    // пустая строка
    ;

// window 10 min;
windowStmt
    : 'window' INT_LITERAL timeUnit ';'
    ;

timeUnit
    : 'ms'
    | 'sec'
    | 'min'
    | 'h'
    ;

// emit mean(s);
emitStmt
    : 'emit' expr ';'
    ;

// x = expr;
assignment
    : ID '=' expr ';'
    ;

// if (expr) { ... } else { ... }
ifStmt
    : 'if' '(' expr ')' block ('else' block)?
    ;

// просто выражение как оператор
exprStmt
    : expr ';'
    ;

// -------------------------
// FSM
// -------------------------

fsmDecl
    : 'fsm' ID '{' stateDecl+ '}'
    ;

stateDecl
    : 'state' ID '{' stateItem* '}'
    ;

stateItem
    : onEnterBlock
    | whenTransition
    ;

onEnterBlock
    : 'on' 'enter' block
    ;

// when expr -> NEXT_STATE;
whenTransition
    : 'when' expr '->' ID ';'
    ;

// -------------------------
// System
// -------------------------

systemDecl
    : 'system' ID '{' systemItem* '}'
    ;

systemItem
    : connectStmt
    | runStmt
    ;

connectStmt
    : 'connect' endpoint '->' endpoint ';'
    ;

endpoint
    : ID ('.' ID)?
    ;

// run fsm greenhouse;
runStmt
    : 'run' 'fsm' ID ';'
    ;

// -------------------------
// Expressions
// -------------------------

expr
    : expr op=('*'|'/') expr                # MulDivExpr
    | expr op=('+'|'-') expr                # AddSubExpr
    | expr op=('<' | '<=' | '>' | '>=') expr # RelExpr
    | expr op=('==' | '!=') expr            # EqExpr
    | expr op=('&&' | '||') expr            # LogicExpr
    | '(' expr ')'                          # ParenExpr
    | functionCall                          # CallExpr
    | primary                               # PrimaryExpr
    ;

functionCall
    : ID '(' (expr (',' expr)*)? ')'
    ;

primary
    : INT_LITERAL
    | FLOAT_LITERAL
    | BOOL_LITERAL
    | STRING_LITERAL
    | ID
    ;

// -------------------------
// Lexer rules
// -------------------------

BOOL_LITERAL
    : 'true'
    | 'false'
    ;

ID
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

fragment DIGIT
    : [0-9]
    ;

INT_LITERAL
    : DIGIT+
    ;

FLOAT_LITERAL
    : DIGIT+ '.' DIGIT* ([eE] [+\-]? DIGIT+)?    // 1.23, 1.23e-4
    | '.' DIGIT+ ([eE] [+\-]? DIGIT+)?          // .5, .5e1
    ;

STRING_LITERAL
    : '"' (~["\\] | '\\' .)* '"'
    ;

// комментарии и пробелы

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
