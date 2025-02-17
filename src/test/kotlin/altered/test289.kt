/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.altered.test289
import org.example.altered.test289.RunChecker289.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce(values: List<Int>) {
        for (value in values) {
            channel.send(value)
        }
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
    }
}

class Processor(private val channel: Channel<Int>) {
    suspend fun process(targetChannel: Channel<Int>) {
        for (i in 1..5) {
            val value = channel.receive()
            targetChannel.send(value + 3)
        }
    }
}

fun producerFunction(channel: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(channel)
    launch(pool) {
        producer.produce(listOf(1, 2, 3, 4, 5))
    }
}

fun consumerFunction(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    val consumer = Consumer(channel1, channel2)
    launch(pool) {
        consumer.consume()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    launch(pool) { 
        producerFunction(channel1)
    }

    launch(pool) {
        consumerFunction(channel1, channel2)
    }

    launch(pool) {
        val processor1 = Processor(channel2)
        processor1.process(channel3)
    }

    launch(pool) {
        val processor2 = Processor(channel3)
        processor2.process(channel4)
    }

    delay(1000L) // Delay to keep the main function alive to see results
}

class RunChecker289: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}