/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test511
import org.example.altered.test511.RunChecker511.Companion.pool
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
        repeat(5) {
            val value = channel.receive()
            println("Consumed: $value")
        }
    }
}

class Processor(private val channel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            channel.send(i * 2)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    
    launch(pool) {
        val producer = Producer(channel)
        producer.produce()
    }

    launch(pool) {
        val consumer = Consumer(channel)
        consumer.consume()
    }

    launch(pool) {
        val processor = Processor(channel)
        processor.process()
    }
}

class RunChecker511: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}