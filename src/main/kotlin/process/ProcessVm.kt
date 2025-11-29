package org.example.process

import fsm.AddFloat
import fsm.BoolVal
import fsm.DivFloat
import fsm.EmitValue
import fsm.EqFloat
import fsm.EqInt
import fsm.FloatVal
import fsm.GeFloat
import fsm.GtFloat
import fsm.IntVal
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
import fsm.Value

/**
 * Стековая VM для процессов: исполняет байткод ProcessProgram (assign/if/emit).
 */
class ProcessVm(private val program: ProcessProgram) {
    private val stack = ArrayDeque<Value>()
    private val env: MutableMap<String, Value> = mutableMapOf()
    private val outputs: MutableMap<String, Value> = mutableMapOf()

    fun run(inputs: Map<String, Value>, maxSteps: Int = 10_000): Map<String, Value> {
        env.clear()
        env.putAll(inputs)
        outputs.clear()

        var pc = 0
        val instrs = program.instrs
        var steps = 0
        while (pc < instrs.size) {
            if (steps++ > maxSteps) error("Process VM exceeded $maxSteps steps, possible infinite loop")
            val ins = instrs[pc]
            when (ins) {
                is PushInt -> stack.addLast(IntVal(ins.value))
                is PushFloat -> stack.addLast(FloatVal(ins.value))
                is PushBool -> stack.addLast(BoolVal(ins.value))
                is LoadVar -> {
                    val v = env[ins.name] ?: error("Var ${ins.name} not set")
                    stack.addLast(v)
                }
                is StoreVar -> {
                    val v = stack.removeLastOrNull() ?: error("StoreVar on empty stack")
                    env[ins.name] = v
                }
                is AddFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("AddFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("AddFloat expects float")
                    stack.addLast(FloatVal(a.value + b.value))
                }
                is SubFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("SubFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("SubFloat expects float")
                    stack.addLast(FloatVal(a.value - b.value))
                }
                is MulFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("MulFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("MulFloat expects float")
                    stack.addLast(FloatVal(a.value * b.value))
                }
                is DivFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("DivFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("DivFloat expects float")
                    stack.addLast(FloatVal(a.value / b.value))
                }
                is GtFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("GtFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("GtFloat expects float")
                    stack.addLast(BoolVal(a.value > b.value))
                }
                is LtFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("LtFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("LtFloat expects float")
                    stack.addLast(BoolVal(a.value < b.value))
                }
                is GeFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("GeFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("GeFloat expects float")
                    stack.addLast(BoolVal(a.value >= b.value))
                }
                is LeFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("LeFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("LeFloat expects float")
                    stack.addLast(BoolVal(a.value <= b.value))
                }
                is EqFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("EqFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("EqFloat expects float")
                    stack.addLast(BoolVal(a.value == b.value))
                }
                is NeqFloat -> {
                    val b = stack.removeLastOrNull() as? FloatVal ?: error("NeqFloat expects float")
                    val a = stack.removeLastOrNull() as? FloatVal ?: error("NeqFloat expects float")
                    stack.addLast(BoolVal(a.value != b.value))
                }
                is EqInt -> {
                    val b = stack.removeLastOrNull() as? IntVal ?: error("EqInt expects int")
                    val a = stack.removeLastOrNull() as? IntVal ?: error("EqInt expects int")
                    stack.addLast(BoolVal(a.value == b.value))
                }
                is NeqInt -> {
                    val b = stack.removeLastOrNull() as? IntVal ?: error("NeqInt expects int")
                    val a = stack.removeLastOrNull() as? IntVal ?: error("NeqInt expects int")
                    stack.addLast(BoolVal(a.value != b.value))
                }
                is Jump -> {
                    pc = ins.target
                    continue
                }
                is JumpIfFalse -> {
                    val v = stack.removeLastOrNull() as? BoolVal ?: error("JumpIfFalse expects bool")
                    if (!v.value) {
                        pc = ins.target
                        continue
                    }
                }
                is EmitValue -> {
                    val v = stack.removeLastOrNull() ?: error("EmitValue on empty stack")
                    outputs[ins.name] = v
                }
                else -> error("Unsupported instruction in ProcessVm: $ins")
            }
            pc += 1
        }

        return outputs.toMap()
    }
}
