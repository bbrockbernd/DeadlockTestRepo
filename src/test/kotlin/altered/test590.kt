/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test590
import org.example.altered.test590.RunChecker590.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            delay(100L)
            channel.send(it)
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

suspend fun startProducing(producer: Producer) {
    producer.produce()
}

suspend fun startConsuming(consumer: Consumer) {
    consumer.consume()
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    launch(pool) { startProducing(producer) }
    launch(pool) { startConsuming(consumer) }

    coroutineScope {
        launch(pool) { startProducing(producer) }
        launch(pool) { startConsuming(consumer) }
    }
}

class RunChecker590: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}