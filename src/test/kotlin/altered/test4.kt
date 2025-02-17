/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":5,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 5 different coroutines
- 4 different classes

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
package org.example.altered.test4
import org.example.altered.test4.RunChecker4.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

class BoundedProducer(val channel: Channel<Int?>) {
    suspend fun produceLimited() {
        for (i in 1..4) {
            channel.send(i)
        }
        channel.send(null)
    }
}

class BoundedConsumer(val channel: Channel<Int?>) {
    suspend fun consumeLimited() {
        while (true) {
            val value = channel.receive() ?: break
        }
    }
}

fun produceA(producer: Producer) {
    runBlocking(pool) {
        launch(pool) {
            producer.produce()
        }
    }
}

fun produceB(boundedProducer: BoundedProducer) {
    runBlocking(pool) {
        launch(pool) {
            boundedProducer.produceLimited()
        }
    }
}

fun consumeA(consumer: Consumer) {
    runBlocking(pool) {
        launch(pool) {
            consumer.consume()
        }
    }
}

fun consumeB(boundedConsumer: BoundedConsumer) {
    runBlocking(pool) {
        launch(pool) {
            boundedConsumer.consumeLimited()
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int?>()
    val channel4 = Channel<Int?>()

    val producerA = Producer(channel1)
    val consumerA = Consumer(channel2)
    val boundedProducerB = BoundedProducer(channel3)
    val boundedConsumerB = BoundedConsumer(channel4)

    runBlocking(pool) {
        launch(pool) {
            produceA(producerA)
        }
        launch(pool) {
            consumeA(consumerA)
        }
        launch(pool) {
            produceB(boundedProducerB)
        }
        launch(pool) {
            consumeB(boundedConsumerB)
        }
        launch(pool) {
            channel2.send(channel1.receive())
        }
    }
}

class RunChecker4: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}