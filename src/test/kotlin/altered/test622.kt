/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test622
import org.example.altered.test622.RunChecker622.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelCoordinator {
    private val channelA = Channel<String>()
    private val channelB = Channel<Int>()
    private val channelC = Channel<Boolean>()
    private val channelD = Channel<Double>()
    private val channelE = Channel<Long>()

    fun setup() {
        runBlocking(pool) {
            launch(pool) { sendToChannelA() }
            launch(pool) { receiveFromChannelA() }
            launch(pool) { sendToChannelB() }
            launch(pool) { receiveFromChannelC() }
            launch(pool) { sendAndReceiveChannelE() }
        }
    }

    suspend fun sendToChannelA() {
        channelA.send("Hello")
    }

    suspend fun receiveFromChannelA() {
        val message = channelA.receive()
        println("Received from channelA: $message")
        channelC.send(true)
    }

    suspend fun sendToChannelB() {
        delay(100)
        channelB.send(42)
    }

    suspend fun receiveFromChannelC() {
        val status = channelC.receive()
        println("Received from channelC: $status")
        channelD.send(3.14)
    }

    suspend fun sendAndReceiveChannelE() {
        channelE.send(100L)
        val resultE = channelE.receive()
        println("Received from channelE: $resultE")
    }
}

fun main(): Unit{
    val coordinator = ChannelCoordinator()
    coordinator.setup()
}

class RunChecker622: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}