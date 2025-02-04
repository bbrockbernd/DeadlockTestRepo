/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 7 different coroutines
- 3 different classes

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
package org.example.altered.test333
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val value = input.receive()
            output.send(value * 2)
        }
    }
}

class ProcessorB(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val value = input.receive()
            output.send(value + 3)
        }
    }
}

class ProcessorC(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val value = input.receive()
            output.send(value - 1)
        }
    }
}

suspend fun produceNumbers(output: Channel<Int>) {
    for (i in 1..5) {
        output.send(i)
    }
}

suspend fun consumeResults(input: Channel<Int>) {
    for (i in 1..5) {
        val result = input.receive()
        println("Received: $result")
    }
}

suspend fun connectProcessors(input: Channel<Int>, inter1: Channel<Int>, inter2: Channel<Int>, output: Channel<Int>) {
    val processorA = ProcessorA(input, inter1)
    val processorB = ProcessorB(inter1, inter2)
    val processorC = ProcessorC(inter2, output)

    coroutineScope {
        launch { processorA.process() }
        launch { processorB.process() }
        launch { processorC.process() }
    }
}

fun main(): Unit= runBlocking {
    val chanA = Channel<Int>()
    val chanB = Channel<Int>()
    val chanC = Channel<Int>()
    val chanD = Channel<Int>()
    val chanE = Channel<Int>()
    val chanF = Channel<Int>()

    launch { produceNumbers(chanA) }
    launch { connectProcessors(chanA, chanB, chanC, chanD) }
    launch { consumeResults(chanD) }
}

class RunChecker333: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}