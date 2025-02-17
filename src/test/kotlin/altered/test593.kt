/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test593
import org.example.altered.test593.RunChecker593.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        val value = channel.receive()
        println("Consumed: $value")
    }
}

suspend fun coroutineOne(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(1)
}

suspend fun coroutineTwo(channel: Channel<Int>) {
    val consumer = Consumer(channel)
    consumer.consume()
}

suspend fun coroutineThree(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(2)
}

suspend fun coroutineFour(channel: Channel<Int>) {
    val consumer = Consumer(channel)
    consumer.consume()
}

suspend fun coroutineFive(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce(3)
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()

    launch(pool) { coroutineOne(channel) }
    launch(pool) { coroutineTwo(channel) }
    launch(pool) { coroutineThree(channel) }
    launch(pool) { coroutineFour(channel) }
    launch(pool) { coroutineFive(channel) }
}

class RunChecker593: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}