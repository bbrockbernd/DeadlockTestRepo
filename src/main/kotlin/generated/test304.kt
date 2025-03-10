/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.generated.test304
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer(channel: Channel<Int>) {
    runBlocking {
        launch {
            for (i in 1..3) {
                channel.send(i)
            }
            // Not closing the channel to induce deadlock
        }
    }
}

fun consumer(name: String, channel: Channel<Int>) {
    runBlocking {
        launch {
            for (i in 1..3) {
                val value = channel.receive()
                println("$name received: $value")
            }
        }
    }
}

fun mainProducer(channel: Channel<Int>) {
    runBlocking {
        launch {
            producer(channel)
        }
    }
}

fun mainConsumer(name: String, channel: Channel<Int>) {
    runBlocking {
        launch {
            consumer(name, channel)
        }
    }
}

fun main(): Unit {
    val channel = Channel<Int>()

    mainProducer(channel)
    mainConsumer("Consumer 1", channel)
    mainConsumer("Consumer 2", channel)
    mainConsumer("Consumer 3", channel)
    mainConsumer("Consumer 4", channel)
    mainConsumer("Consumer 5", channel)
}

main(): Unit