/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 7 different coroutines
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
package org.example.generated.test193
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()
    val channelD = Channel<Boolean>()
    val channelE = Channel<Long>()
}

suspend fun sendToChannelA(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun sendToChannelB(channel: Channel<String>, value: String) {
    channel.send(value)
}

suspend fun sendToChannelC(channel: Channel<Double>, value: Double) {
    channel.send(value)
}

suspend fun sendToChannelD(channel: Channel<Boolean>, value: Boolean) {
    channel.send(value)
}

suspend fun sendToChannelE(channel: Channel<Long>, value: Long) {
    channel.send(value)
}

suspend fun receiveFromChannelA(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun receiveFromChannelB(channel: Channel<String>): String {
    return channel.receive()
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch {
        sendToChannelA(manager.channelA, 1)
    }
    launch {
        sendToChannelB(manager.channelB, "Hello")
    }
    launch {
        sendToChannelC(manager.channelC, 3.14)
    }
    launch {
        sendToChannelD(manager.channelD, true)
    }
    launch {
        sendToChannelE(manager.channelE, 42L)
    }
    launch {
        val valueA = receiveFromChannelA(manager.channelA)
        println("Received from channel A: $valueA")
    }
    launch {
        val valueB = receiveFromChannelB(manager.channelB)
        println("Received from channel B: $valueB")
    }
}