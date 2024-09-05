/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
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
package org.example.generated.test64
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun functionA(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    val job1 = launch {
        for (i in 1..5) {
            channel1.send(i)
            delay(100)
        }
        channel1.close()
    }
    val job2 = launch {
        for (i in channel1) {
            channel2.send(i * 2)
        }
        channel2.close()
    }
}

fun functionB(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    val job3 = launch {
        for (i in 1..5) {
            channel3.send(i)
            delay(50)
        }
        channel3.close()
    }
    val job4 = launch {
        for (i in channel3) {
            channel4.send(i + 1)
        }
        channel4.close()
    }
}

fun functionC(channel5: Channel<Int>, channel6: Channel<Int>) = runBlocking {
    val job5 = launch {
        for (i in 1..5) {
            channel5.send(i)
            delay(75)
        }
        channel5.close()
    }
    val job6 = launch {
        for (i in channel5) {
            channel6.send(i - 1)
        }
        channel6.close()
    }
}

fun functionD(channel7: Channel<Int>, channel8: Channel<Int>) = runBlocking {
    val job7 = launch {
        for (i in 1..5) {
            channel7.send(i)
            delay(125)
        }
        channel7.close()
    }
    val job8 = launch {
        for (i in channel7) {
            channel8.send(i * i)
        }
        channel8.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    functionA(channel1, channel2)
    functionB(channel3, channel4)
    functionC(channel5, channel6)
    functionD(channel7, channel8)

    val results = listOf(channel2, channel4, channel6, channel8).map { channel ->
        launch {
            for (result in channel) {
                println("Received: $result")
            }
        }
    }

    results.forEach { it.join() }
}