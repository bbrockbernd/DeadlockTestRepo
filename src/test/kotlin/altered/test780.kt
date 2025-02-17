/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 4 different coroutines
- 0 different classes

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
package org.example.altered.test780
import org.example.altered.test780.RunChecker780.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>, id: Int) = runBlocking(pool) {
    launch(pool) {
        repeat(5) { 
            channel.send(id * 10 + it)
            delay(100L)
        }
        channel.close()
    }
}

fun consumer(channel: Channel<Int>, id: Int) = runBlocking(pool) {
    launch(pool) {
        for (value in channel) {
            println("Consumer $id received: $value")
            delay(150L)
        }
    }
}

suspend fun relay(inputChannel: Channel<Int>, outputChannel: Channel<Int>) = coroutineScope {
    launch(pool) {
        for (value in inputChannel) {
            outputChannel.send(value)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelOne = Channel<Int>()
    val channelTwo = Channel<Int>()

    launch(pool) {
        producer(channelOne, 1)
    }

    launch(pool) {
        producer(channelOne, 2)
    }

    launch(pool) {
        relay(channelOne, channelTwo)
    }

    launch(pool) {
        consumer(channelTwo, 1)
    }
}

class RunChecker780: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}