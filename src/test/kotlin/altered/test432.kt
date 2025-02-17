/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
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
package org.example.altered.test432
import org.example.altered.test432.RunChecker432.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produceValues() {
        repeat(5) {
            channel1.send(it)
        }
        repeat(5) {
            channel2.send(it * 2)
        }
    }
}

class ProducerB(val channel3: Channel<String>, val channel4: Channel<String>) {
    suspend fun produceValues() {
        repeat(5) {
            channel3.send("A$it")
        }
        repeat(5) {
            channel4.send("B$it")
        }
    }
}

class Consumer(private val channel5: Channel<Int>, private val channel6: Channel<String>, private val channel7: Channel<Int>, private val channel8: Channel<String>) {
    suspend fun consumeValues() {
        repeat(5) {
            println("Consumer received from channel5: ${channel5.receive()}")
        }
        repeat(5) {
            println("Consumer received from channel6: ${channel6.receive()}")
        }
        repeat(5) {
            println("Consumer received from channel7: ${channel7.receive()}")
        }
        repeat(5) {
            println("Consumer received from channel8: ${channel8.receive()}")
        }
    }
}

fun functionA(channel1: Channel<Int>, channel2: Channel<Int>): ProducerA {
    return ProducerA(channel1, channel2)
}

fun functionB(channel3: Channel<String>, channel4: Channel<String>): ProducerB {
    return ProducerB(channel3, channel4)
}

fun functionC(channel5: Channel<Int>, channel6: Channel<String>, channel7: Channel<Int>, channel8: Channel<String>): Consumer {
    return Consumer(channel5, channel6, channel7, channel8)
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<String>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<String>()

    val producerA = functionA(channel1, channel2)
    val producerB = functionB(channel3, channel4)
    val consumer = functionC(channel5, channel6, channel7, channel8)

    launch(pool) {
        producerA.produceValues()
    }

    launch(pool) {
        producerB.produceValues()
    }

    launch(pool) {
        repeat(5) { 
            channel5.send(channel1.receive() + 10)
            channel7.send(channel2.receive() + 20)
        }
    }

    launch(pool) {
        repeat(5) {
            channel6.send("C${channel3.receive()}")
            channel8.send("D${channel4.receive()}")
        }
    }

    launch(pool) {
        consumer.consumeValues()
    }
}

class RunChecker432: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}