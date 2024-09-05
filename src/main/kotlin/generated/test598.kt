/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.generated.test598
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

suspend fun process(channel: Channel<Int>, consumer: Consumer) {
    val result = consumer.consume()
    channel.send(result * 2)
}

fun transform(channel: Channel<Int>, producer: Producer, consumer: Consumer) {
    runBlocking {
        launch {
            producer.produce()
        }

        launch {
            for (i in 1..5) {
                process(channel, consumer)
            }
        }

        launch {
            process(channel, consumer)
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    transform(channel, producer, consumer)
}