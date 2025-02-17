/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test391
import org.example.altered.test391.RunChecker391.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    fun startProducing() {
        GlobalScope.launch(pool) {
            for (i in 1..5) {
                channelA.send(i)
                channelB.send(i * 2)
            }
        }
    }
}

class ConsumerA(val channelA: Channel<Int>) {
    suspend fun consume() {
        coroutineScope {
            launch(pool) {
                for (i in 1..5) {
                    println("ConsumerA received from channelA: ${channelA.receive()}")
                }
            }
        }
    }
}

class ConsumerB(val channelB: Channel<Int>) {
    suspend fun consume() {
        coroutineScope {
            launch(pool) {
                for (i in 1..5) {
                    println("ConsumerB received from channelB: ${channelB.receive()}")
                }
            }
        }
    }
}

fun runProducer(producer: Producer) {
    producer.startProducing()
}

suspend fun runConsumers(consumerA: ConsumerA, consumerB: ConsumerB) {
    coroutineScope {
        launch(pool) { consumerA.consume() }
        launch(pool) { consumerB.consume() }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>() // Unbuffered channel A
    val channelB = Channel<Int>() // Unbuffered channel B

    val producer = Producer(channelA, channelB)
    val consumerA = ConsumerA(channelA)
    val consumerB = ConsumerB(channelB)

    runProducer(producer)
    runConsumers(consumerA, consumerB)
}

class RunChecker391: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}