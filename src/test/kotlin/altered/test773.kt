/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test773
import org.example.altered.test773.RunChecker773.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelCommunicator(private val channel: Channel<Int>) {
    suspend fun sendValues() {
        repeat(5) {
            channel.send(it)
        }
    }

    suspend fun receiveValues() {
        repeat(5) {
            println("Received: ${channel.receive()}")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    val communicator = ChannelCommunicator(channel)

    launch(pool) {
        initiateCommunication(communicator)
    }

    launch(pool) {
        repeat(5) {
            channel.send(it + 10)
        }
    }

    launch(pool) {
        val communicator2 = ChannelCommunicator(channel)
        communicator2.receiveValues()
    }
}

suspend fun initiateCommunication(communicator: ChannelCommunicator) = coroutineScope {
    launch(pool) {
        communicator.sendValues()
    }

    launch(pool) {
        communicator.receiveValues()
    }
}

class RunChecker773: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}