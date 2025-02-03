/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 4 different coroutines
- 2 different classes

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
- lists, arrays or other datastructures
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
package org.example.altered.test910
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        val data = input.receive()
        output.send(data * 2)
    }
}

class ProcessorB(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        val data = input.receive()
        output.send(data + 3)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val processorA = ProcessorA(channel1, channel2)
    val processorB = ProcessorB(channel2, channel3)

    launch {
        processorA.process()
    }

    launch {
        channel1.send(1)
    }

    launch {
        processorB.process()
    }

    launch {
        val result = channel3.receive()
        println("Result: $result")
    }
}

class RunChecker910: RunCheckerBase() {
    override fun block() = main()
}