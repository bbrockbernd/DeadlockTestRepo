/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test801
import org.example.altered.test801.RunChecker801.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager(
    private val channelA: Channel<Int>,
    private val channelB: Channel<Int>,
    private val channelC: Channel<String>,
    private val channelD: Channel<String>
) {
    suspend fun sendDataToChannelA(data: Int) {
        channelA.send(data)
    }

    suspend fun sendDataToChannelB(data: Int) {
        channelB.send(data)
    }

    suspend fun sendDataToChannelC(data: String) {
        channelC.send(data)
    }

    suspend fun sendDataToChannelD(data: String) {
        channelD.send(data)
    }
}

fun receiveAndSendIntData(channelManager: ChannelManager, inputChannel: Channel<Int>, outputChannel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val data = inputChannel.receive()
        channelManager.sendDataToChannelA(data)
    }
    launch(pool) {
        val data = inputChannel.receive()
        channelManager.sendDataToChannelB(data)
    }
}

fun receiveAndSendStringData(channelManager: ChannelManager, inputChannel: Channel<String>, outputChannel: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        val data = inputChannel.receive()
        channelManager.sendDataToChannelC(data)
    }
    launch(pool) {
        val data = inputChannel.receive()
        channelManager.sendDataToChannelD(data)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    
    val channelManager = ChannelManager(channel1, channel2, channel3, channel4)
    
    launch(pool) {
        receiveAndSendIntData(channelManager, channel1, channel2)
    }
    
    launch(pool) {
        receiveAndSendStringData(channelManager, channel3, channel4)
    }
    
    // Simulate data sending
    launch(pool) {
        channel1.send(10)
        channel1.send(20)
    }

    launch(pool) {
        channel3.send("Hello")
        channel3.send("World")
    }
}

class RunChecker801: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}