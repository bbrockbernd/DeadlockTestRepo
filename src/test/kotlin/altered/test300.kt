/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test300
import org.example.altered.test300.RunChecker300.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel = Channel<Int>()
}

suspend fun sendToChannel(channelManager: ChannelManager) {
    channelManager.channel.send(1)
}

suspend fun receiveFromChannel(channelManager: ChannelManager): Int {
    return channelManager.channel.receive()
}

suspend fun coroutine1(channelManager: ChannelManager) {
    sendToChannel(channelManager)
    println("Coroutine 1 sent to channel")
}

suspend fun coroutine2(channelManager: ChannelManager) {
    val value = receiveFromChannel(channelManager)
    println("Coroutine 2 received from channel: $value")
}

suspend fun coroutine3(channelManager: ChannelManager) {
    coroutineScope {
        launch(pool) { coroutine1(channelManager) }
        launch(pool) { coroutine2(channelManager) }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelManager = ChannelManager()
    coroutineScope {
        launch(pool) { coroutine1(channelManager) }
        launch(pool) { coroutine3(channelManager) }
    }
}

class RunChecker300: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}