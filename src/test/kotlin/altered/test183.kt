/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test183
import org.example.altered.test183.RunChecker183.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun produceA() {
        for (i in 1..5) {
            channelA.send(i)
        }
    }

    suspend fun produceB() {
        for (i in 6..10) {
            channelB.send(i)
        }
    }
}

class Consumer(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun consumeA() {
        for (i in 1..5) {
            println("Consumer A received: ${channelA.receive()}")
        }
    }

    suspend fun consumeB() {
        for (i in 6..10) {
            println("Consumer B received: ${channelB.receive()}")
        }
    }
}

suspend fun processChannels(channelA: Channel<Int>, channelB: Channel<Int>): Int {
    val sumA = empty(channelA)
    val sumB = empty(channelB)
    return sumA + sumB
}

suspend fun empty(channel: Channel<Int>): Int {
    var sum = 0
    for (element in 1..5) {
        sum += channel.receive()
    }
    return sum
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val producer = Producer(channelA, channelB)
    val consumer = Consumer(channelA, channelB)

    launch(pool) {
        producer.produceA()
        producer.produceB()
    }

    launch(pool) {
        consumer.consumeA()
        consumer.consumeB()
    }

    println("Sum of all elements: ${processChannels(channelA, channelB)}")
}

class RunChecker183: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}