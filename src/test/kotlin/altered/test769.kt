/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 1 different coroutines
- 2 different classes

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
package org.example.altered.test769
import org.example.altered.test769.RunChecker769.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SenderA(private val channel: Channel<Int>) {
    suspend fun sendA() {
        channel.send(1)
    }
}

class SenderB(private val channel: Channel<Int>) {
    suspend fun sendB() {
        channel.send(2)
    }
}

fun receiveFromChannelA(channel: Channel<Int>): Int {
    return runBlocking(pool) {
        channel.receive()
    }
}

fun receiveFromChannelB(channel: Channel<Int>): Int {
    return runBlocking(pool) {
        channel.receive()
    }
}

suspend fun processChannels(channelA: Channel<Int>, channelB: Channel<Int>) {
    val valueA = receiveFromChannelA(channelA)
    val valueB = receiveFromChannelB(channelB)

    println("Received from Channel A: $valueA")
    println("Received from Channel B: $valueB")
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val senderA = SenderA(channelA)
    val senderB = SenderB(channelB)

    launch(pool) {
        senderA.sendA()
        senderB.sendB()
    }

    // Creating a deadlock by waiting for each other indefinitely
    launch(pool) {
        processChannels(channelA, channelB)
    }
}

class RunChecker769: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}