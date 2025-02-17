/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
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
package org.example.altered.test833
import org.example.altered.test833.RunChecker833.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (x in 1..5) {
            channel.send(x)
        }
        channel.close()
    }
}

class Consumer(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun consume() {
        for (y in inputChannel) {
            outputChannel.send(y * 2)
        }
        outputChannel.close()
    }
}

class Printer(val channel: Channel<Int>) {
    suspend fun print() {
        for (z in channel) {
            println(z)
        }
    }
}

fun initializeChannels(): List<Channel<Int>> {
    return listOf(
        Channel(),
        Channel(),
        Channel(),
        Channel(),
        Channel()
    )
}

fun main(): Unit= runBlocking(pool) {
    val (channel1, channel2, channel3, channel4, channel5) = initializeChannels()

    launch(pool) {
        Producer(channel1).produce()
    }

    launch(pool) {
        Consumer(channel1, channel2).consume()
    }

    launch(pool) {
        Consumer(channel2, channel3).consume()
    }

    launch(pool) {
        Printer(channel3).print()
    }

    coroutineScope {
        val dummyChannel = Channel<Int>()
        val producer = Producer(dummyChannel)
        producer.produce()
    }
}

class RunChecker833: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}