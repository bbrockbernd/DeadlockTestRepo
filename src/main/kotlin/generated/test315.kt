/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 2 different channels
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
package org.example.generated.test315
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..10) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): List<Int> {
        val result = mutableListOf<Int>()
        for (i in channel) {
            result.add(i)
        }
        return result
    }
}

fun provideChannel(): Channel<Int> {
    return Channel(10)
}

suspend fun executeProducer(producer: Producer) {
    coroutineScope {
        producer.produce()
    }
}

suspend fun executeConsumer(consumer: Consumer): List<Int> {
    return coroutineScope {
        consumer.consume()
    }
}

fun processItem(item: Int): Int {
    return item * 2
}

suspend fun processReceivedItems(items: List<Int>): List<Int> {
    return items.map { processItem(it) }
}

fun main(): Unit = runBlocking {
    val channelOne = provideChannel()
    val producer = Producer(channelOne)
    val consumer = Consumer(channelOne)

    launch { executeProducer(producer) }
    val result = executeConsumer(consumer)

    val processedItems = processReceivedItems(result)
    processedItems.forEach { println(it) }
}