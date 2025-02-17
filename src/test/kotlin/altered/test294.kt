/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test294
import org.example.altered.test294.RunChecker294.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val id: Int, val outputChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            delay(100L)
            outputChannel.send(id * 10 + i)
        }
    }
}

class Consumer(val id: Int, val inputChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = inputChannel.receive()
            println("Consumer $id received $value")
        }
    }
}

fun processChannel(inputChannel: Channel<Int>, outputChannel: Channel<Int>): Job = GlobalScope.launch(pool) {
    for (i in 1..5) {
        val value = inputChannel.receive()
        outputChannel.send(value * 2)
    }
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<Int>(5)
    val channel4 = Channel<Int>(5)
    val channel5 = Channel<Int>(5)
    val channel6 = Channel<Int>(5)
    val channel7 = Channel<Int>(5)
    val channel8 = Channel<Int>(5)

    val producer1 = Producer(1, channel1)
    val producer2 = Producer(2, channel3)
    val producer3 = Producer(3, channel5)

    val consumer1 = Consumer(1, channel2)
    val consumer2 = Consumer(2, channel4)

    launch(pool) { producer1.produce() }
    launch(pool) { producer2.produce() }
    launch(pool) { producer3.produce() }

    launch(pool) { processChannel(channel1, channel2) }
    launch(pool) { processChannel(channel3, channel4) }
    launch(pool) { processChannel(channel5, channel6) }
    launch(pool) { processChannel(channel6, channel7) }
    launch(pool) { processChannel(channel7, channel8) }

    launch(pool) { consumer1.consume() }
    launch(pool) { consumer2.consume() }

    delay(10000L) // Wait for all coroutines to finish
}

class RunChecker294: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}