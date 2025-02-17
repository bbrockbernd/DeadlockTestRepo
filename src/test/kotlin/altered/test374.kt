/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":7,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.altered.test374
import org.example.altered.test374.RunChecker374.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
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
        for (i in 1..5) {
            channel.receive()
        }
    }
}

fun produceData(channel: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(channel)
    producer.produce()
}

fun consumeData(channel: Channel<Int>) = runBlocking(pool) {
    val consumer = Consumer(channel)
    consumer.consume()
}

fun main(): Unit = runBlocking(pool) {
    val channel = Channel<Int>()

    launch(pool) {
        produceData(channel)
    }

    launch(pool) {
        consumeData(channel)
    }

    launch(pool) {
        produceData(channel)
    }

    launch(pool) {
        consumeData(channel)
    }

    launch(pool) {
        produceData(channel)
    }

    launch(pool) {
        consumeData(channel)
    }

    launch(pool) {
        produceData(channel)
    }
}

class RunChecker374: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}