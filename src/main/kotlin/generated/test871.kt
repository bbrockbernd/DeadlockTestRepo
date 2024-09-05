/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.generated.test871
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Producing: $i")
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Consuming: $value")
        }
    }
}

suspend fun coroutine1(channel: Channel<Int>, producer: Producer) {
    coroutineScope {
        launch {
            producer.produce()
            println("Producer done")
        }
        channel.send(100) // Extra send to cause deadlock
    }
}

suspend fun coroutine2(channel: Channel<Int>, consumer: Consumer) {
    coroutineScope {
        launch {
            consumer.consume()
            println("Consumer done")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    launch {
        coroutine1(channel, producer)
    }

    launch {
        coroutine2(channel, consumer)
    }
}