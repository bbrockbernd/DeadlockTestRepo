/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test593
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        val value = channel.receive()
        println("Consumed: $value")
    }
}

suspend fun coroutineOne(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(1)
}

suspend fun coroutineTwo(channel: Channel<Int>) {
    val consumer = Consumer(channel)
    consumer.consume()
}

suspend fun coroutineThree(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(2)
}

suspend fun coroutineFour(channel: Channel<Int>) {
    val consumer = Consumer(channel)
    consumer.consume()
}

suspend fun coroutineFive(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(3)
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { coroutineOne(channel) }
    launch { coroutineTwo(channel) }
    launch { coroutineThree(channel) }
    launch { coroutineFour(channel) }
    launch { coroutineFive(channel) }
}