/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 6 different channels
- 6 different coroutines
- 1 different classes

You ARE ALLOWED to use basic Kotlin constructs and coroutine primitives. A few examples are:
- functions and suspend functions
- dot qualified expressions
- class properties
- local variables
- unbuffered and buffered channels
- channel init, send and receive
- return values
- function composition (nested calls)
- runBlocking and launch builder
- coroutineScope

You ARE NOT ALLOWED to use more complex features like:
- joins
- async builder
- mutability
- nullability
- for (i in channel)
- flow
- lateinit
- lazyval
- inheritance
- lambdas with arguments
- mutexes 
*/
package org.example.altered.test385
import org.example.altered.test385.RunChecker385.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val input = Channel<Int>(1)
    val output1 = Channel<Int>(1)
    val output2 = Channel<Int>(1)
    val intermed1 = Channel<Int>(1)
    val intermed2 = Channel<Int>(1)
}

fun main(): Unit = runBlocking(pool) {
    val processor = Processor()
    launch(pool) { function1(processor) }
    launch(pool) { function2(processor.input, processor.intermed1) }
    launch(pool) { function3(processor.intermed1, processor.output1) }
    launch(pool) { function4(processor.output1, processor.output2) }
    launch(pool) { function5(processor.output2, processor.intermed2) }
    launch(pool) { function6(processor.intermed2, processor.input) }
    
    processor.input.send(1)
    println(processor.output1.receive())
    println(processor.output2.receive())
    processor.input.close()
}

suspend fun function1(processor: Processor) {
    processor.input.send(3)
}

suspend fun function2(input: Channel<Int>, intermed1: Channel<Int>) {
    val value = input.receive() + 2
    intermed1.send(value)
}

suspend fun function3(intermed1: Channel<Int>, output1: Channel<Int>) {
    val value = intermed1.receive() * 2
    output1.send(value)
}

suspend fun function4(output1: Channel<Int>, output2: Channel<Int>) {
    val value = output1.receive() / 2
    output2.send(value)
}

suspend fun function5(output2: Channel<Int>, intermed2: Channel<Int>) {
    val value = output2.receive() - 1
    intermed2.send(value)
}

suspend fun function6(intermed2: Channel<Int>, input: Channel<Int>) {
    val value = intermed2.receive()
    input.send(value)
}

class RunChecker385: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}