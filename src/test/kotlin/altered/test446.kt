/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 8 different coroutines
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
package org.example.altered.test446
import org.example.altered.test446.RunChecker446.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<String>()
    val channel4 = Channel<String>(1)
    val channel5 = Channel<Boolean>()
    val channel6 = Channel<Boolean>(1)
    val channel7 = Channel<Double>()
    val channel8 = Channel<Double>(1)
}

fun main(): Unit= runBlocking(pool) {
    val manager = ChannelManager() 

    launch(pool) { processIntChannel(manager.channel1, manager.channel2) }
    launch(pool) { processStringChannel(manager.channel3, manager.channel4) }
    launch(pool) { processBooleanChannel(manager.channel5, manager.channel6) }
    launch(pool) { processDoubleChannel(manager.channel7, manager.channel8) }
    launch(pool) { sendNumbers(manager.channel1) }
    launch(pool) { sendStrings(manager.channel3) }
    launch(pool) { sendBooleans(manager.channel5) }
    launch(pool) { sendDoubles(manager.channel7) }
}

suspend fun processIntChannel(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    coroutineScope {
        launch(pool) {
            val received = channelIn.receive()
            channelOut.send(received * 2)
        }
    }
}

suspend fun processStringChannel(channelIn: Channel<String>, channelOut: Channel<String>) {
    coroutineScope {
        launch(pool) {
            val received = channelIn.receive()
            channelOut.send(received.reversed())
        }
    }
}

suspend fun processBooleanChannel(channelIn: Channel<Boolean>, channelOut: Channel<Boolean>) {
    coroutineScope {
        launch(pool) {
            val received = channelIn.receive()
            channelOut.send(!received)
        }
    }
}

suspend fun processDoubleChannel(channelIn: Channel<Double>, channelOut: Channel<Double>) {
    coroutineScope {
        launch(pool) {
            val received = channelIn.receive()
            channelOut.send(received + 1.0)
        }
    }
}

suspend fun sendNumbers(channel: Channel<Int>) {
    coroutineScope {
        launch(pool) {
            channel.send(42)
        }
    }
}

suspend fun sendStrings(channel: Channel<String>) {
    coroutineScope {
        launch(pool) {
            channel.send("Hello")
        }
    }
}

suspend fun sendBooleans(channel: Channel<Boolean>) {
    coroutineScope {
        launch(pool) {
            channel.send(true)
        }
    }
}

suspend fun sendDoubles(channel: Channel<Double>) {
    coroutineScope {
        launch(pool) {
            channel.send(3.14)
        }
    }
}

class RunChecker446: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}