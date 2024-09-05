/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.generated.test740
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (value in channel) {
            println("Consumed: $value")
        }
    }
}

fun producerFunc(channel: Channel<Int>) = runBlocking {
    val producer = Producer(channel)

    launch {
        producer.produce()
    }
}

fun consumerFunc(channel: Channel<Int>) = runBlocking {
    val consumer = Consumer(channel)

    launch {
        consumer.consume()
    }
}

fun main(): Unit{
    val channel = Channel<Int>()

    runBlocking {
        launch {
            producerFunc(channel)
        }

        launch {
            consumerFunc(channel)
        }

        launch {
            producerFunc(channel)
        }

        launch {
            consumerFunc(channel)
        }
    }
}