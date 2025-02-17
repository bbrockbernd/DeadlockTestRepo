/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
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
package org.example.altered.test562
import org.example.altered.test562.RunChecker562.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<String>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send("Message $i")
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<String>) {
    suspend fun consume() {
        for (msg in channel) {
            println("Consumed: $msg")
        }
    }
}

class Aggregator(val inputChannel: Channel<String>, val outputChannel: Channel<String>) {
    suspend fun aggregate() {
        for (msg in inputChannel) {
            outputChannel.send("Aggregated $msg")
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val producerChannel = Channel<String>()
    val aggregatorChannel = Channel<String>()

    val producer = Producer(producerChannel)
    val consumer = Consumer(aggregatorChannel)
    val aggregator = Aggregator(producerChannel, aggregatorChannel)

    launch(pool) { producer.produce() }
    launch(pool) { aggregator.aggregate() }
    launch(pool) { consumer.consume() }
}

class RunChecker562: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}