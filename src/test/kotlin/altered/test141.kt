/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test141
import org.example.altered.test141.RunChecker141.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..4) {
            channel.send(i)
            delay(100)
        }
    }
}

class ConsumerA(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(2) {
            val value = channel.receive()
            println("ConsumerA received: $value")
        }
    }
}

class ConsumerB(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(2) {
            val value = channel.receive()
            println("ConsumerB received: $value")
        }
    }
}

fun startProducer(channel: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(channel)
    launch(pool) { producer.produce() }
}

fun startConsumers(channel: Channel<Int>) = runBlocking(pool) {
    val consumerA = ConsumerA(channel)
    val consumerB = ConsumerB(channel)
    launch(pool) { consumerA.consume() }
    launch(pool) { consumerA.consume() }
    launch(pool) { consumerB.consume() }
    launch(pool) { consumerB.consume() }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    launch(pool) { startProducer(channel) }
    launch(pool) { startConsumers(channel) }
}

class RunChecker141: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}