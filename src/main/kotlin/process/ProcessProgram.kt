package org.example.process

import TypeRef
import fsm.Instr

/**
 * Скомпилированный процесс: сигнатура входов/выходов и линейный байткод.
 */
data class ProcessProgram(
    val name: String,
    val inputs: Map<String, TypeRef>,
    val outputs: Map<String, TypeRef>,
    val instrs: List<Instr>
)
