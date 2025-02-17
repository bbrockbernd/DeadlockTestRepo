/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test359
import org.example.altered.test359.RunChecker359.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val id: Int, val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(id * 10 + i)
        }
        output.close()
    }
}

class Consumer(val id: Int, val input: Channel<Int>) {
    suspend fun consume() {
        for (value in input) {
            println("Consumer $id received: $value")
        }
    }
}

class Coordinator(val channels: List<Channel<Int>>) {
    suspend fun coordinate() {
        coroutineScope {
            channels.forEach { channel ->
                launch(pool) {
                    for (msg in channel) {
                        println("Coordinator received: $msg")
                    }
                }
            }
        }
    }
}

fun createChannels(): List<Channel<Int>> {
    return List(8) { Channel<Int>() }
}

fun main(): Unit= runBlocking(pool) {
    val channels = createChannels()

    val producer1 = Producer(1, channels[0])
    val producer2 = Producer(2, channels[1])
    val producer3 = Producer(3, channels[2])
    val producer4 = Producer(4, channels[3])

    val consumer1 = Consumer(1, channels[4])
    val consumer2 = Consumer(2, channels[5])
    val consumer3 = Consumer(3, channels[6])
    val consumer4 = Consumer(4, channels[7])

    val coordinator = Coordinator(channels)

    launch(pool) { producer1.produce() }
    launch(pool) { producer2.produce() }
    launch(pool) { producer3.produce() }
    launch(pool) { producer4.produce() }
    launch(pool) { consumer1.consume() }
    launch(pool) { consumer2.consume() }
    launch(pool) { consumer3.consume() }
    launch(pool) { consumer4.consume() }
    launch(pool) { coordinator.coordinate() }
}

class RunChecker359: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}