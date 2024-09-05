/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.generated.test861
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

suspend fun producerFunc(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce()
}

suspend fun consumerFunc(channel: Channel<Int>) {
    val consumer = Consumer(channel)
    consumer.consume()
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch { producerFunc(channel) }
    launch { consumerFunc(channel) }
}