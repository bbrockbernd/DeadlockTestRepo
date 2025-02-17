/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test795
import org.example.altered.test795.RunChecker795.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun produce() {
        channelA.send(1)
        channelB.receive()
    }
}

class Consumer {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun consume() {
        channelC.receive()
        channelD.send(2)
    }
}

fun main(): Unit= runBlocking(pool) {
    val producer = Producer()
    val consumer = Consumer()

    launch(pool) {
        producer.produce()
    }

    launch(pool) {
        consumer.consume()
    }

    producer.channelA.receive()
    consumer.channelC.send(3)
    producer.channelB.send(4)
    consumer.channelD.receive()
}

class RunChecker795: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}