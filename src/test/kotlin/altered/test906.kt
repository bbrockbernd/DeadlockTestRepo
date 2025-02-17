/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.altered.test906
import org.example.altered.test906.RunChecker906.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

class Processor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun process() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

suspend fun performOperation(producer: Producer, consumer: Consumer, processor: Processor) = coroutineScope {
    launch(pool) { producer.produce() }
    launch(pool) { processor.process() }
    launch(pool) { consumer.consume() }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel2)
    val processor = Processor(channel1, channel2)

    launch(pool) { performOperation(producer, consumer, processor) }
    launch(pool) { performOperation(producer, consumer, processor) }
    launch(pool) { performOperation(producer, consumer, processor) }
}

class RunChecker906: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}