/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.generated.test780
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>, id: Int) = runBlocking {
    launch {
        repeat(5) { 
            channel.send(id * 10 + it)
            delay(100L)
        }
        channel.close()
    }
}

fun consumer(channel: Channel<Int>, id: Int) = runBlocking {
    launch {
        for (value in channel) {
            println("Consumer $id received: $value")
            delay(150L)
        }
    }
}

suspend fun relay(inputChannel: Channel<Int>, outputChannel: Channel<Int>) = coroutineScope {
    launch {
        for (value in inputChannel) {
            outputChannel.send(value)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channelOne = Channel<Int>()
    val channelTwo = Channel<Int>()

    launch {
        producer(channelOne, 1)
    }

    launch {
        producer(channelOne, 2)
    }

    launch {
        relay(channelOne, channelTwo)
    }

    launch {
        consumer(channelTwo, 1)
    }
}