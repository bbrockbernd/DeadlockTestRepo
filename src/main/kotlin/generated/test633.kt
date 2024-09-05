/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.generated.test633
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        channel1.send(1)
        channel2.send(2)
    }
}

fun consumer(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        val received = channel2.receive()
        channel3.send(received)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch {
        producer(channel1, channel2)
    }

    launch {
        consumer(channel2, channel3)
    }

    launch {
        val receivedFromChannel1 = channel1.receive()
        println("Received from Channel 1: $receivedFromChannel1")
        // Attempt to send to channel3
        channel3.send(receivedFromChannel1)
    }

    launch {
        val receivedFromChannel3 = channel3.receive()
        println("Received from Channel 3: $receivedFromChannel3")
    }
}