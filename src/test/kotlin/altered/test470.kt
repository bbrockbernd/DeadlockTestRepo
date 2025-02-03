/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.altered.test470
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>(1)
    val channel5 = Channel<Int>(1)
    val channel6 = Channel<String>(1)
    val channel7 = Channel<Int>(1)
    val channel8 = Channel<String>()

    suspend fun sendValues() {
        channel1.send(42)
        channel2.send("Kotlin")
        channel3.send(Kotlin.random.Random.nextInt(100))
    }

    suspend fun receiveValues() {
        val value1 = channel1.receive()
        val value2 = channel2.receive()
        val value3 = channel3.receive()
        println(value1)
        println(value2)
        println(value3)
    }

    suspend fun processValues() {
        val value1 = channel4.receive()
        channel5.send(value1 + 1)
        val value2 = channel6.receive()
        channel7.send(value2.toInt() + 1)
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch {
        manager.sendValues()
    }

    launch {
        manager.receiveValues()
    }

    launch {
        manager.processValues()
    }

    // Utilize additional channels causing a deadlock scenario
    launch {
        manager.channel4.send(10)
        val value = manager.channel5.receive()
        manager.channel6.send(value.toString())
        manager.channel8.send("Deadlock")
    }

    launch {
        val received = manager.channel8.receive()
        println(received)
    }
}

class RunChecker470: RunCheckerBase() {
    override fun block() = main()
}