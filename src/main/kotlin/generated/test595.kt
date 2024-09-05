/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 4 different coroutines
- 0 different classes

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
package org.example.generated.test595
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun sendToChannel(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun receiveFromChannel(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun sendAndReceive(ch1: Channel<Int>, ch2: Channel<Int>, value: Int): Int {
    sendToChannel(ch1, value)
    return receiveFromChannel(ch2)
}

suspend fun processChannels(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>): Int {
    val value1 = sendAndReceive(ch1, ch2, 10)
    val value2 = sendAndReceive(ch2, ch3, value1)
    return sendAndReceive(ch3, ch1, value2)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch {
        processChannels(channel1, channel2, channel3)
    }
    launch {
        sendToChannel(channel2, receiveFromChannel(channel1))
    }
    launch {
        sendToChannel(channel3, receiveFromChannel(channel2))
    }
    launch {
        sendToChannel(channel1, receiveFromChannel(channel3))
    }

    delay(1000L)  // Delay to simulate a running program and observe the deadlock
}