/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test890
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor1 {
    suspend fun process(channel: Channel<Int>, value: Int) {
        channel.send(value)
        val result = channel.receive()
        println("Processor1 received: $result")
    }
}

class Processor2 {
    suspend fun process(channel: Channel<Int>, value: Int) {
        channel.send(value * 2)
        val result = channel.receive()
        println("Processor2 received: $result")
    }
}

class Coordinator(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun coordinate() {
        val value = channel1.receive()
        channel2.send(value + 10)
        println("Coordinator processed: $value")
    }
}

suspend fun initiateProcesses(channel1: Channel<Int>, channel2: Channel<Int>) {
    val processor1 = Processor1()
    val processor2 = Processor2()
    val coordinator = Coordinator(channel1, channel2)
    
    coroutineScope {
        launch { processor1.process(channel1, 1) }
        launch { processor2.process(channel2, 2) }
        launch { coordinator.coordinate() }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { initiateProcesses(channel1, channel2) }
    launch {
        val result = channel1.receive()
        val adjusted = result + 2
        println("Main coroutine adjusted value: $adjusted")
    }
}

class RunChecker890: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}