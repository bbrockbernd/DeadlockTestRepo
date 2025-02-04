/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test808
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(2)
    val channel5 = Channel<Int>()

    suspend fun sendValues() {
        channel1.send(1)
        channel2.send(2)
        channel2.send(3)
        channel3.send(4)
    }

    suspend fun receiveValues(): Int {
        val value1 = channel1.receive()
        val value2 = channel2.receive()
        val value3 = channel3.receive()
        return value1 + value2 + value3
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch {
        manager.sendValues()
        val result = manager.receiveValues()
        println("Result: $result")
    }

    val extraJob = launch {
        manager.channel4.send(5)
        val value = manager.channel4.receive()
        println("Received from channel4: $value")
    }

    manager.channel5.send(6)
    val valueFromChannel5 = manager.channel5.receive()
    println("Received from channel5: $valueFromChannel5")

    extraJob.join()
}

class RunChecker808: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}