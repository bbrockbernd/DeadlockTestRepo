/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
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
package org.example.generated.test90
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

fun initProducerChannel(): Channel<Int> {
    return Channel()
}

fun initConsumerChannel(): Channel<Int> {
    return Channel()
}

suspend fun produceValues(producer: Producer) {
    producer.produce(1)
    producer.produce(2)
}

suspend fun consumeValues(consumer: Consumer): Int {
    return consumer.consume() + consumer.consume()
}

suspend fun transferValues(producer: Producer, consumer: Consumer) {
    produceValues(producer)
    consumeValues(consumer)
}

fun triggerDeadlock(producer: Producer, consumer: Consumer) {
    runBlocking {
        launch {
            transferValues(producer, consumer)
        }
    }
}

fun main(): Unit{
    val producerChannel = initProducerChannel()
    val consumerChannel = initConsumerChannel()

    val producer = Producer(producerChannel)
    val consumer = Consumer(consumerChannel)

    // Deadlock occurs here because both produce and consume are waiting on each other
    triggerDeadlock(producer, consumer)
}