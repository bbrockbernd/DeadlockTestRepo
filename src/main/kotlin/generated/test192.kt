/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test192
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer1(channel: Channel<Int>) = runBlocking {
    repeat(5) {
        delay(100L)
        channel.send(it)
    }
    channel.close()
}

fun producer2(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    for (i in channel1) {
        delay(200L)
        channel2.send(i * i)
    }
    channel2.close()
}

fun consumer(channel: Channel<Int>) = runBlocking {
    for (value in channel) {
        delay(150L)
        println("Consumed: $value")
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch {
        producer1(channel1)
    }

    launch {
        producer2(channel1, channel2)
    }

    launch {
        for (i in 1..5) {
            delay(50L)
            channel3.send(i * 10)
        }
        channel3.close()
    }

    launch {
        consumer(channel2)
    }

    consumer(channel3)
}