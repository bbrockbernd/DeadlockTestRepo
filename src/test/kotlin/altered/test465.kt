/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test465
import org.example.altered.test465.RunChecker465.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produce1() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }

    suspend fun produce2() {
        for (i in 6..10) {
            channel2.send(i)
        }
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun consume1() {
        for (i in 1..5) {
            println("Consumed from channel1: ${channel1.receive()}")
        }
    }

    suspend fun consume2() {
        for (i in 6..10) {
            println("Consumed from channel2: ${channel2.receive()}")
        }
    }
}

class Middleware(
    private val channel1: Channel<Int>,
    private val channel2: Channel<Int>,
    private val channel3: Channel<Int>
) {
    suspend fun transfer1() {
        val value = channel1.receive()
        channel3.send(value)
    }

    suspend fun transfer2() {
        val value = channel2.receive()
        channel3.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel3, channel4)
    val middleware = Middleware(channel1, channel2, channel3)

    launch(pool) { producer.produce1() }
    launch(pool) { producer.produce2() }
    launch(pool) { middleware.transfer1() }
    launch(pool) { middleware.transfer2() }
    launch(pool) { consumer.consume1() }
    launch(pool) { consumer.consume2() }
    
    // Deadlock coroutine
    launch(pool) {
        channel4.send(channel3.receive())
    }
}

class RunChecker465: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}