/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test998
import org.example.altered.test998.RunChecker998.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(3) {
            channel.receive()
        }
    }
}

fun producerFunction(channel1: Channel<Int>, channel2: Channel<Int>) {
    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)

    runBlocking(pool) {
        launch(pool) {
            producer1.produce()
        }
        launch(pool) {
            producer2.produce()
        }
    }
}

fun consumerFunction(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) {
    val consumer1 = Consumer(channel3)
    val consumer2 = Consumer(channel4)

    runBlocking(pool) {
        launch(pool) {
            consumer1.consume()
            channel5.send(1)
        }
        launch(pool) {
            consumer2.consume()
            channel5.receive()
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    runBlocking(pool) {
        launch(pool) {
            producerFunction(channel1, channel2)
        }
        launch(pool) {
            consumerFunction(channel3, channel4, channel5)
        }
    }
}

class RunChecker998: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}