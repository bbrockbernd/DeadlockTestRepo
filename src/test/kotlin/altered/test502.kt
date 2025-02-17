/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test502
import org.example.altered.test502.RunChecker502.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// First class
class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        // Do not close the channel to create a potential deadlock scenario
        // channel.close()
    }
}

// Second class
class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Received: $value")
        }
    }
}

// Third class
class Manager(private val producer: Producer, private val consumer: Consumer) {
    suspend fun coordinate() {
        coroutineScope {
            launch(pool) {
                producer.produce()
            }
            launch(pool) {
                consumer.consume()
            }
        }
    }
}

// Functions

suspend fun setupProducer(channel1: Channel<Int>): Producer {
    return Producer(channel1)
}

suspend fun setupConsumer(channel2: Channel<Int>): Consumer {
    return Consumer(channel2)
}

suspend fun setupManager(producer: Producer, consumer: Consumer): Manager {
    return Manager(producer, consumer)
}

// Main function to run everything
fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = setupProducer(channel1)
    val consumer = setupConsumer(channel2)

    val manager = setupManager(producer, consumer)
    manager.coordinate()
}

class RunChecker502: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}