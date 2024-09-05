/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
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
package org.example.generated.test696
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer1(channel: Channel<Int>) {
    runBlocking {
        repeat(5) {
            channel.send(it)
        }
    }
}

fun producer2(channel: Channel<Int>) {
    runBlocking {
        repeat(5) {
            channel.send(it + 5)
        }
    }
}

fun consumer1(channel: Channel<Int>) {
    runBlocking {
        repeat(5) {
            println("Consumer1 received: ${channel.receive()}")
        }
    }
}

fun consumer2(channel: Channel<Int>) {
    runBlocking {
        repeat(5) {
            println("Consumer2 received: ${channel.receive()}")
        }
    }
}

fun main(): Unit{
    runBlocking {
        val channel1 = Channel<Int>()
        val channel2 = Channel<Int>()

        launch { producer1(channel1) }
        launch { producer2(channel2) }
        launch { consumer1(channel1) }
        launch { consumer2(channel2) }
    }
}