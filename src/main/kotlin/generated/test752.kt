/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.generated.test752
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch {
        coroutine1(channel1, channel2)
    }
    launch {
        coroutine2(channel3, channel4)
    }
    launch {
        coroutine3(channel5, channel1, channel2)
    }
    launch {
        coroutine4(channel3, channel4, channel5)
    }
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    val received = channel2.receive()
    println("coroutine1 received: $received")
}

suspend fun coroutine2(channel3: Channel<Int>, channel4: Channel<Int>) {
    channel4.send(2)
    val received = channel3.receive()
    println("coroutine2 received: $received")
}

suspend fun coroutine3(channel5: Channel<Int>, channel1: Channel<Int>, channel2: Channel<Int>) {
    val received1 = channel5.receive()
    val received2 = channel1.receive()
    println("coroutine3 received: $received1 and $received2")
    channel2.send(3)
}

suspend fun coroutine4(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) {
    val received1 = channel3.receive()
    val received2 = channel4.receive()
    println("coroutine4 received: $received1 and $received2")
    channel5.send(4)
}