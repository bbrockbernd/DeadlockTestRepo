/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test937
import org.example.altered.test937.RunChecker937.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>) {
        while (true) {
            val item = channel.receive()
            println("Received $item")
        }
    }
}

suspend fun producerFunction(channel: Channel<Int>) = coroutineScope {
    val producer = Producer()
    producer.produce(channel)
}

suspend fun consumerFunction(channel: Channel<Int>) = coroutineScope {
    val consumer = Consumer()
    consumer.consume(channel)
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    
    launch(pool) {
        producerFunction(channel)
    }
    
    launch(pool) {
        consumerFunction(channel)
    }

    launch(pool) {
        produceAndConsume(channel)
    }

    launch(pool) {
        val consumer = Consumer()
        consumer.consume(channel)
    }
}

suspend fun produceAndConsume(channel: Channel<Int>) = coroutineScope {
    val producer = Producer()
    producer.produce(channel)
    val consumer = Consumer()
    consumer.consume(channel)
}

class RunChecker937: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}