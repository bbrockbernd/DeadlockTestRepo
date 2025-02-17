/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test425
import org.example.altered.test425.RunChecker425.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    
    suspend fun producer1() {
        channel1.send(1)
        channel2.send(channel1.receive() + 1)
        channel3.send(channel2.receive() + 1)
    }
    
    suspend fun producer2() {
        channel4.send(channel3.receive() + 1)
        channel5.send(channel4.receive() + 1)
    }
    
    suspend fun producer3() {
        channel6.send(channel5.receive() + 1)
        channel7.send(channel6.receive() + 1)
        println("Received in channel7: ${channel7.receive()}")
    }
}

fun main(): Unit= runBlocking(pool) {
    val manager = ChannelManager()

    launch(pool) {
        manager.producer1()
    }
    
    launch(pool) {
        manager.producer2()
    }
    
    launch(pool) {
        manager.producer3()
    }
}

class RunChecker425: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}