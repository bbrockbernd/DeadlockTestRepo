/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 8 different coroutines
- 4 different classes

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
package org.example.altered.test348
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProcessorA(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun processData() {
        for (item in input) {
            output.send(item * 2)
        }
    }
}

class ProcessorB(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun processData() {
        for (item in input) {
            output.send(item + 5)
        }
    }
}

class ProcessorC(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun processData() {
        for (item in input) {
            output.send(item - 3)
        }
    }
}

class FinalProcessor(val input1: Channel<Int>, val input2: Channel<Int>, val output: Channel<Int>) {
    suspend fun combine() {
        val result1 = input1.receive()
        val result2 = input2.receive()
        output.send(result1 + result2)
    }
}

suspend fun startProcessingA(input: Channel<Int>, output: Channel<Int>) {
    val processor = ProcessorA(input, output)
    processor.processData()
}

suspend fun startProcessingB(input: Channel<Int>, output: Channel<Int>) {
    val processor = ProcessorB(input, output)
    processor.processData()
}

suspend fun startProcessingC(input: Channel<Int>, output: Channel<Int>) {
    val processor = ProcessorC(input, output)
    processor.processData()
}

fun main(): Unit= runBlocking {
    val input1 = Channel<Int>()
    val input2 = Channel<Int>()
    val input3 = Channel<Int>()
    val outputA = Channel<Int>()
    val outputB = Channel<Int>()
    val finalOutput = Channel<Int>()

    launch {
        startProcessingA(input1, outputA)
    }

    launch {
        startProcessingB(input2, outputA)
    }

    launch {
        startProcessingC(input3, outputB)
    }

    launch {
        while (true) {
            outputA.send(outputB.receive())
        }
    }

    launch {
        outputB.send(0)
        outputA.send(0)
    }

    launch {
        val finalProcessor = FinalProcessor(outputA, outputB, finalOutput)
        finalProcessor.combine()
    }

    launch {
        input1.send(10)
    }

    launch {
        input2.send(20)
    }

    launch {
        input3.send(30)
    }
}

class RunChecker348: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}