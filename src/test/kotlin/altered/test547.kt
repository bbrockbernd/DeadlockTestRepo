/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.altered.test547
import org.example.altered.test547.RunChecker547.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
}

class Processor(val channelManager: ChannelManager) {
    suspend fun processChannel1() {
        val data = channelManager.channel1.receive()
        channelManager.channel2.send(data)
    }

    suspend fun processChannel2() {
        val data = channelManager.channel2.receive()
        channelManager.channel3.send(data)
    }
}

class DataProvider {
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    suspend fun provideData(channelManager: ChannelManager) {
        channelManager.channel1.send(1)
        val data = channel4.receive()
        channelManager.channel2.send(data)
    }

    suspend fun consumeData(channelManager: ChannelManager) {
        val data = channelManager.channel3.receive()
        channel5.send(data)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelManager = ChannelManager()
    val processor = Processor(channelManager)
    val dataProvider = DataProvider()

    launch(pool) {
        processor.processChannel1()
    }

    launch(pool) {
        processor.processChannel2()
    }

    launch(pool) {
        dataProvider.provideData(channelManager)
    }

    launch(pool) {
        dataProvider.consumeData(channelManager)
    }
}

class RunChecker547: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}