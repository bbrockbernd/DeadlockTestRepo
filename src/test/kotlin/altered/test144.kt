/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test144
import org.example.altered.test144.RunChecker144.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    fun produce() {
        runBlocking(pool) {
            repeat(5) {
                channel.send(it)
            }
            channel.close()
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    fun consume() {
        runBlocking(pool) {
            for (i in channel) {
                println("Consumed: $i")
            }
        }
    }
}

class Coordinator(private val producer: Producer, private val consumer: Consumer) {
    fun coordinate() {
        runBlocking(pool) {
            launch(pool) {
                producer.produce()
            }
            launch(pool) {
                consumer.consume()
            }
        }
    }
}

fun main(): Unit {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val coordinator = Coordinator(producer, consumer)

    startCoroutines(coordinator)
}

fun startCoroutines(coordinator: Coordinator) {
    coordinator.coordinate()
}

fun simpleFunction() {
    println("This is a simple function")
}

class RunChecker144: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}