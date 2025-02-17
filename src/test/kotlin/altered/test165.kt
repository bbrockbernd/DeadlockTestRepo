/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":7,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test165
import org.example.altered.test165.RunChecker165.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
}

class Sender(private val channelManager: ChannelManager) {
    fun startSending() = runBlocking(pool) {
        launch(pool) {
            channelManager.channel1.send(1)
            channelManager.channel2.send(2)
        }
        launch(pool) {
            channelManager.channel3.send(3)
            channelManager.channel4.send(4)
        }
    }
}

class Receiver(private val channelManager: ChannelManager) {
    fun startReceiving() = runBlocking(pool) {
        launch(pool) {
            val received1 = channelManager.channel1.receive()
            val received2 = channelManager.channel2.receive()
        }
        launch(pool) {
            val received3 = channelManager.channel3.receive()
            val received4 = channelManager.channel4.receive()
        }
    }
}

class Processor(private val channelManager: ChannelManager) {
    fun process() = runBlocking(pool) {
        launch(pool) {
            val data1 = channelManager.channel1.receive()
            val result1 = data1 * 2
            channelManager.channel5.send(result1)
        }
        launch(pool) {
            val data2 = channelManager.channel3.receive()
            val result2 = data2 * 3
            channelManager.channel5.send(result2)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelManager = ChannelManager()
    val sender = Sender(channelManager)
    val receiver = Receiver(channelManager)
    val processor = Processor(channelManager)

    launch(pool) { sender.startSending() }
    launch(pool) { receiver.startReceiving() }
    launch(pool) { processor.process() }

    launch(pool) {
        repeat(2) {
            val finalResult = channelManager.channel5.receive()
        }
    }
}

class RunChecker165: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}