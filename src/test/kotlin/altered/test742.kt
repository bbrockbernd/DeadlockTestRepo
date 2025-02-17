/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test742
import org.example.altered.test742.RunChecker742.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun initChannels() {
        CoroutineScope(pool).launch(pool) {
            sendToChannel1()
        }
        CoroutineScope(pool).launch(pool) {
            sendToChannel2()
        }
        CoroutineScope(pool).launch(pool) {
            receiveFromChannel3()
        }
        CoroutineScope(pool).launch(pool) {
            sendToChannel4()
        }
    }

    suspend fun sendToChannel1() {
        channel2.send(1)
        channel1.receive()
    }

    suspend fun sendToChannel2() {
        channel3.send(2)
        channel2.receive()
    }

    suspend fun receiveFromChannel3() {
        channel4.send(3)
        channel3.receive()
    }

    suspend fun sendToChannel4() {
        channel1.send(4)
        channel4.receive()
    }
}

fun main(): Unit= runBlocking(pool) {
    val handler = ChannelHandler()
    handler.initChannels()
    delay(5000)  // Allow some time to observe the deadlock
}

class RunChecker742: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}