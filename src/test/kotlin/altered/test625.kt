/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.altered.test625
import org.example.altered.test625.RunChecker625.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

fun producerFunction(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val producer1 = Producer(channel1)
        val producer2 = Producer(channel2)
        for (i in 1..5) {
            producer1.produce(i)
            producer2.produce(i + 5)
        }
    }
}

fun consumerFunction(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val consumer1 = Consumer(channel3)
        val consumer2 = Consumer(channel4)
        val consumer3 = Consumer(channel5)
        repeat(5) {
            println(consumer1.consume())
            println(consumer2.consume())
            println(consumer3.consume())
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch(pool) { producerFunction(channel1, channel2) }
    launch(pool) { consumerFunction(channel3, channel4, channel5) }

    val relayProducer1 = launch(pool) {
        for (i in 1..5) {
            val value = channel1.receive()
            channel3.send(value)
        }
    }

    val relayProducer2 = launch(pool) {
        for (i in 1..5) {
            val value = channel2.receive()
            channel4.send(value)
        }
    }

    val relayProducer3 = launch(pool) {
        for (i in 1..5) {
            val value = channel1.receive()
            channel5.send(value)
        }
    }

    relayProducer1.join()
    relayProducer2.join()
    relayProducer3.join()
}

class RunChecker625: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}