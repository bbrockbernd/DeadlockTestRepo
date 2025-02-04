/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.altered.test119
import org.example.altered.RunCheckerBase
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
    suspend fun consume() {
        repeat(5) {
            val item = channel.receive()
        }
    }
}

class Processor(private val producer: Producer, private val consumer: Consumer) {
    suspend fun process() {
        coroutineScope {
            producer.produce()
            consumer.consume()
        }
    }
}

fun startProducer(producer: Producer) {
    runBlocking {
        launch {
            producer.produce()
        }
    }
}

fun startConsumer(consumer: Consumer) {
    runBlocking {
        launch {
            consumer.consume()
        }
    }
}

fun startProcessor(processor: Processor) {
    runBlocking {
        launch {
            processor.process()
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val processor = Processor(producer, consumer)

    startProducer(producer)
    startConsumer(consumer)
    startProcessor(processor)
}

class RunChecker119: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}