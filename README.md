# GreenLang — язык для «умной теплицы»

Прототип языка с тремя моделями вычислений:
- **FSM** — конечные автоматы (`fsm/state/on enter/when`), работают на мини‑VM.
- **Сеть процессов (dataflow / Kahn)** — `process` с `stream<T>` и `connect`; TempController компилируется в байткод процесса и кормит FSM.
- **Императивная** — присваивания/`if`/арифметика внутри процессов и `on enter`.

## Как запустить демо
Требования: JDK 21.

```bash
./gradlew runFsm
```
Или использовать методы main:
- Main.kt - для AST дерева
- FsmMain.kt - для демострации работы моделей

Что увидите: вывод байткода `TempController`, затем прогон t=[25.0,29.0,18.0,23.0] → mode, переключения FSM (DAY→VENT→HEAT→DAY) и выставление актуаторов.

## Структура проекта
- `src/main/antlr/GreenLang.g4` — грамматика языка (sensors/actuators/process/fsm/system, выражения, if/assign/emit).
- `src/main/kotlin/ast.kt` — AST: верхнеуровневые декларации, типы (`StreamType`), выражения (`VarRef/IntLit/FloatLit/BoolLit/BinOp`), стейтменты (`Assign/Emit/IfStmt`).
- `src/main/kotlin/AstBuilder.kt` — visitor ANTLR → AST.
- `src/main/kotlin/fsm/*` — байткод/компилятор/VM для FSM: `FsmBytecode.kt`, `FsmCompiler.kt`, `FsmVm.kt`.
- `src/main/kotlin/process/*` — байткод/компилятор/VM для процессов: `ProcessProgram.kt`, `ProcessCompiler.kt`, `ProcessVm.kt`.
- `src/main/kotlin/FsmMain.kt` — демо: парсит `example.green`, строит AST, компилирует TempController → байткод процесса, прогоняет его на t‑значениях, передаёт mode в FSM VM, печатает переходы/актуаторы.
- `src/main/resources/example.green` — пример программы для теплицы (датчики temp/humid, актуаторы heater/fan/light, процессы AvgTemp/TempController, FSM GreenhouseFSM, system со связками).

## Модели вычислений
- **FSM**: описываются `fsm { state ... }`, семантика — один активный state, переход по первому истинному `when`, `on enter` выполняет действия (у нас — булевые присваивания актуаторам). Реализация: AST → `FsmProgram` → `FsmVm.step()`.
- **Сеть процессов (dataflow/Kahn)**: `process ... (in/out stream<T>)` и `connect` в `system`. Реализация MVP: TempController компилируется в байткод процесса (Assign/If/Emit), `ProcessVm` исполняет и эмитит `mode`, который подаётся в FSM. Каналы/планировщик для нескольких процессов — следующая итерация, но базовая связка продемонстрирована.
- **Императивная**: внутри процессов и `on enter`/условий используются переменные, присваивания, `if`, арифметика. Компилируется в стековый байткод (Push/Load/Store, арифметика, Jump/JumpIfFalse) и исполняется VM.
