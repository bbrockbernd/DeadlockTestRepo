/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.generated.test569
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(channel: Channel<Int>, nextChannel: Channel<Int>) {
    runBlocking {
        launch {
            val value = channel.receive()
            nextChannel.send(value + 1)
        }
    }
}

fun functionB(channel: Channel<Int>, nextChannel: Channel<Int>) {
    runBlocking {
        launch {
            val value = channel.receive()
            nextChannel.send(value + 2)
        }
    }
}

fun functionC(channel: Channel<Int>, nextChannel: Channel<Int>) {
    runBlocking {
        launch {
            val value = channel.receive()
            nextChannel.send(value + 3)
        }
    }
}

fun functionD(channel: Channel<Int>, nextChannel: Channel<Int>) {
    runBlocking {
        launch {
            val value = channel.receive()
            nextChannel.send(value + 4)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    runBlocking {
        // Creating coroutines
        launch { functionA(channel1, channel2) }
        launch { functionB(channel2, channel3) }
        launch { functionC(channel3, channel4) }
        launch { functionD(channel4, channel1) }

        // Starting the deadlock situation
        launch {
            channel1.send(0)
        }
    }
}