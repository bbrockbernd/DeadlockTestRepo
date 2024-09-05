/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.generated.test873
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer1(channel: Channel<Int>, nextChannel: Channel<Int>) {
    GlobalScope.launch {
        for (x in 1..5) {
            channel.send(x)
            nextChannel.send(x)
        }
    }
}

fun producer2(channel: Channel<Int>, nextChannel: Channel<Int>) {
    GlobalScope.launch {
        for (x in 6..10) {
            channel.send(x)
            nextChannel.send(x)
        }
    }
}

fun processor(channel: Channel<Int>, nextChannel: Channel<Int>, resultChannel: Channel<Int>) {
    GlobalScope.launch {
        repeat(5) {
            val received = channel.receive()
            nextChannel.send(received + 10)
            resultChannel.send(received)
        }
    }
}

fun mainProcessor(channel1: Channel<Int>, channel2: Channel<Int>, resultChannel: Channel<Int>) {
    GlobalScope.launch {
        repeat(10) {
            val received1 = channel1.receive()
            val received2 = channel2.receive()
            resultChannel.send(received1 + received2)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val resultChannel = Channel<Int>(20) // Buffered to prevent fast blocking

    producer1(channel1, channel3)
    producer2(channel2, channel4)
    processor(channel3, channel4, resultChannel)
    mainProcessor(channel1, channel2, resultChannel)

    repeat(20) {
        println(resultChannel.receive())
    }
}