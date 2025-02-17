/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
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
package org.example.altered.test368
import org.example.altered.test368.RunChecker368.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (x in 1..5) {
            channel.send(x)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (x in channel) {
            println("Consumed $x")
        }
    }
}

class Coordinator(
    private val producerChannel1: Channel<Int>,
    private val producerChannel2: Channel<Int>,
    private val consumerChannel1: Channel<Int>,
    private val consumerChannel2: Channel<Int>
) {

    suspend fun coordinate() {
        coroutineScope {
            launch(pool) {
                repeat(5) {
                    val value1 = producerChannel1.receive()
                    val value2 = producerChannel2.receive()
                    consumerChannel1.send(value1 + value2)
                }
                consumerChannel1.close()
            }

            launch(pool) {
                repeat(5) {
                    val result = consumerChannel1.receive()
                    consumerChannel2.send(result * 2)
                }
                consumerChannel2.close()
            }
        }
    }
}

fun produceToChannel1(chan: Channel<Int>): Producer {
    return Producer(chan)
}

fun produceToChannel2(chan: Channel<Int>): Producer {
    return Producer(chan)
}

fun consumerFromChannel1(chan: Channel<Int>): Consumer {
    return Consumer(chan)
}

fun consumerFromChannel2(chan: Channel<Int>): Consumer {
    return Consumer(chan)
}

fun main(): Unit= runBlocking(pool) {
    val producerChannel1 = Channel<Int>()
    val producerChannel2 = Channel<Int>()
    val consumerChannel1 = Channel<Int>()
    val consumerChannel2 = Channel<Int>()

    val producer1 = produceToChannel1(producerChannel1)
    val producer2 = produceToChannel2(producerChannel2)
    val consumer1 = consumerFromChannel1(consumerChannel2)
    
    val coordinator = Coordinator(producerChannel1, producerChannel2, consumerChannel1, consumerChannel2)

    launch(pool) { producer1.produce() }
    launch(pool) { producer2.produce() }
    launch(pool) { coordinator.coordinate() }
    launch(pool) { consumer1.consume() }
}

class RunChecker368: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}