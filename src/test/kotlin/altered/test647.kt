/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 3 different coroutines
- 1 different classes

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
package org.example.altered.test647
import org.example.altered.test647.RunChecker647.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    suspend fun process(channel1: Channel<Int>, channel2: Channel<Int>) {
        channel1.send(channel2.receive())
    }

    suspend fun job(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
        val data1 = channel1.receive()
        val data2 = channel2.receive()
        channel3.send(data1 + data2)
    }
}

suspend fun senderFunction(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun receiverFunction(channel: Channel<Int>): Int {
    return channel.receive()
}

fun triggerDeadlock() {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    val processor = Processor()

    runBlocking(pool) {
        launch(pool) {
            processor.process(channelA, channelB)
        }

        launch(pool) {
            processor.job(channelA, channelB, channelC)
        }

        launch(pool) {
            senderFunction(channelB, 10)
            val result = receiverFunction(channelC)
            println("Received: $result")
        }
    }
}

fun main(): Unit{
    triggerDeadlock()
}

class RunChecker647: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}