/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test665
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val inputChannel: Channel<Int>, val outputChannel: Channel<String>) {
    suspend fun process() {
        for (x in 1..5) {
            val received = inputChannel.receive()
            outputChannel.send("Processed A: $received")
        }
    }
}

class ProcessorB(val inputChannel: Channel<String>, val outputChannel: Channel<Double>) {
    suspend fun process() {
        for (x in 1..5) {
            val received = inputChannel.receive()
            outputChannel.send(received.length * 2.0)
        }
    }
}

class ProcessorC(val channel1: Channel<Int>, val channel2: Channel<String>, val channel3: Channel<Double>) {
    fun startProcessing() = runBlocking {
        val coroutine1 = launch { producer(channel1) }
        val coroutine2 = launch { ProcessorA(channel1, channel2).process() }
        val coroutine3 = launch { ProcessorB(channel2, channel3).process() }
        val coroutine4 = launch { consumer3(channel3) }
        val coroutine5 = launch { consumer(channel1, channel2) }
    }

    suspend fun producer(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
    }

    suspend fun consumer(channel1: Channel<Int>, channel2: Channel<String>) {
        channel1.receive()
        channel2.receive()
    }

    suspend fun consumer3(channel: Channel<Double>) {
        for (x in 1..5) {
            println("Consumed C: ${channel.receive()}")
        }
    }
}

fun main(): Unit{
    ProcessorC(Channel(), Channel(), Channel()).startProcessing()
}

class RunChecker665: RunCheckerBase() {
    override fun block() = main()
}