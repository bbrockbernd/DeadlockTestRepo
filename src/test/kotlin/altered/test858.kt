/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.altered.test858
import org.example.altered.test858.RunChecker858.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun consume() {
        for (i in 1..5) {
            val receivedValue = channel1.receive()
            channel2.send("Received: $receivedValue")
        }
        channel2.close()
    }
}

fun CoroutineScope.launchProducers(channel: Channel<Int>) {
    launch(pool) {
        val producer = Producer(channel)
        producer.produce()
    }
}

fun CoroutineScope.launchConsumers(channel1: Channel<Int>, channel2: Channel<String>) {
    launch(pool) {
        val consumer = Consumer(channel1, channel2)
        consumer.consume()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    launchProducers(channel1)
    launchConsumers(channel1, channel2)

    launch(pool) {
        for (result in channel2) {
            println(result)
        }
    }
}

class RunChecker858: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}