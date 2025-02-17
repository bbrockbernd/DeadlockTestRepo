/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.altered.test744
import org.example.altered.test744.RunChecker744.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun sendToChannelA(channelA: Channel<Int>, value: Int) {
    channelA.trySend(value)
}

fun receiveFromChannelA(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking(pool) {
        val value = channelA.receive()
        sendToChannelB(channelB, value + 1)
    }
}

fun sendToChannelB(channelB: Channel<Int>, value: Int) {
    channelB.trySend(value)
}

fun receiveFromChannelB(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking(pool) {
        val value = channelB.receive()
        sendToChannelC(channelC, value + 1)
    }
}

fun sendToChannelC(channelC: Channel<Int>, value: Int) {
    channelC.trySend(value)
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)
    val channelC = Channel<Int>(1)

    launch(pool) {
        sendToChannelA(channelA, 0)
        receiveFromChannelA(channelA, channelB)
    }

    launch(pool) {
        receiveFromChannelB(channelB, channelC)
    }
}

class RunChecker744: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}