/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 8 different coroutines
- 5 different classes

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
package org.example.altered.test40
import org.example.altered.test40.RunChecker40.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(private val channel: Channel<Int>) {
    suspend fun process(value: Int) {
        channel.send(value)
    }
}

class Aggregator(private val channel: Channel<Int>) {
    suspend fun aggregate(): Int = coroutineScope {
        var sum = 0
        for (element in channel) {
            sum += element
        }
        sum
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (element in channel) {
            println("Consumed: $element")
        }
    }
}

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Transformer(private val channel: Channel<Int>) {
    suspend fun transform() {
        for (element in channel) {
            channel.send(element * 2)
        }
    }
}

class Logger(private val channel: Channel<Int>) {
    suspend fun log() {
        for (element in channel) {
            println("Logged: $element")
        }
    }
}

class Finalizer(private val channel: Channel<Int>) {
    suspend fun finalize() {
        for (element in channel) {
            println("Finalized: $element")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>(10)

    val processor = Processor(channel)
    val aggregator = Aggregator(channel)
    val consumer = Consumer(channel)
    val producer = Producer(channel)
    val transformer = Transformer(channel)
    val logger = Logger(channel)
    val finalizer = Finalizer(channel)

    launch(pool) { producer.produce() }
    launch(pool) { processor.process(5) }
    launch(pool) { transformer.transform() }
    launch(pool) { logger.log() }
    launch(pool) { consumer.consume() }
    launch(pool) { finalizer.finalize() }
    launch(pool) { println("Aggregated sum: ${aggregator.aggregate()}") }
}

class RunChecker40: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}