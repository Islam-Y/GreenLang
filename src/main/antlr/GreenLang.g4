//формальное описание синтаксиса языка (грамматика)

grammar GreenLang;

// -------------------------
// Parser rules
// -------------------------

// точка входа программы
program
    : topLevelDecl* EOF
    ;

// верхнеуровневое объявление
topLevelDecl
    : sensorDecl
    | actuatorDecl
    | processDecl
    | fsmDecl
    | systemDecl
    ;

// объявление сенсора
sensorDecl
    : 'sensor' ID ':' typeRef 'from' STRING_LITERAL ';'
    ;

// объявление актуатора
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

// объявление процесса
processDecl
    : 'process' ID '(' processParams? ')' block
    ;

// список параметров процесса
processParams
    : processParam (',' processParam)*
    ;

// параметр процесса
processParam
    : ('in' | 'out') ID ':' 'stream' '<' typeRef '>'
    ;

// блок операторов
block
    : '{' statement* '}'
    ;

// оператор
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

// единицы времени
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

// объявление конечного автомата
fsmDecl
    : 'fsm' ID '{' stateDecl+ '}'
    ;

// объявление состояния
stateDecl
    : 'state' ID '{' stateItem* '}'
    ;

// элемент состояния
stateItem
    : onEnterBlock
    | whenTransition
    ;

// обработчик входа в состояние
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

// объявление системы
systemDecl
    : 'system' ID '{' systemItem* '}'
    ;

// элемент системы
systemItem
    : connectStmt
    | runStmt
    ;

// соединение компонентов
connectStmt
    : 'connect' endpoint '->' endpoint ';'
    ;

// ссылка на endpoint
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

// выражение с учетом приоритетов
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

// вызов функции
functionCall
    : ID '(' (expr (',' expr)*)? ')'
    ;

// первичное значение
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

// булев литерал
BOOL_LITERAL
    : 'true'
    | 'false'
    ;

// идентификатор
ID
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

// цифра
fragment DIGIT
    : [0-9]
    ;

// целое число
INT_LITERAL
    : DIGIT+
    ;

// вещественное число
FLOAT_LITERAL
    : DIGIT+ '.' DIGIT* ([eE] [+\-]? DIGIT+)?    // 1.23, 1.23e-4
    | '.' DIGIT+ ([eE] [+\-]? DIGIT+)?          // .5, .5e1
    ;

// строковый литерал
STRING_LITERAL
    : '"' (~["\\] | '\\' .)* '"'
    ;

// комментарии и пробелы

// однострочный комментарий
LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

// блочный комментарий
BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

// пробельные символы
WS
    : [ \t\r\n]+ -> skip
    ;
