/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
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
package org.example.altered.test836
import org.example.altered.test836.RunChecker836.Companion.pool
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

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in channel) {
            println("Received: $i")
        }
    }
}

class Coordinator(private val chan1: Channel<Int>, private val chan2: Channel<Int>, private val chan3: Channel<Int>) {
    suspend fun coordinate() {
        coroutineScope {
            launch(pool) {
                for (i in chan1) {
                    chan2.send(i * 2)
                }
                chan2.close()
            }
            launch(pool) {
                for (i in chan2) {
                    chan3.send(i + 1)
                }
                chan3.close()
            }
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()

    val producer = Producer(chan1)
    val consumer = Consumer(chan3)
    val coordinator = Coordinator(chan1, chan2, chan3)

    launch(pool) { producer.produce() }
    launch(pool) { coordinator.coordinate() }
    launch(pool) { consumer.consume() }
}

class RunChecker836: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}