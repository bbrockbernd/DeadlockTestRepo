/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test846
import org.example.altered.test846.RunChecker846.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val outChannel: Channel<Int>, val inChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            outChannel.send(i)
            inChannel.receive()
        }
    }
}

class Consumer(val inChannel: Channel<Int>, val outChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..3) {
            inChannel.receive()
            outChannel.send(0)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer1 = Producer(channel1, channel2)
    val producer2 = Producer(channel3, channel4)
    val consumer1 = Consumer(channel2, channel3)
    val consumer2 = Consumer(channel4, channel1)

    launch(pool) {
        producer1.produce()
    }
    launch(pool) {
        producer2.produce()
    }
    launch(pool) {
        consumer1.consume()
    }
    launch(pool) {
        consumer2.consume()
    }
}

class RunChecker846: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}