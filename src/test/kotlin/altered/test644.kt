/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test644
import org.example.altered.test644.RunChecker644.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channelA = Channel<Int>()
val channelB = Channel<Int>()
val channelC = Channel<Int>()
val channelD = Channel<Int>()

fun sendToChannels() = runBlocking(pool) {
    launch(pool) {
        val value = channelA.receive()
        channelB.send(value)
        channelC.send(value)
    }
    launch(pool) {
        val value = channelB.receive()
        channelD.send(value)
    }
}

fun receiveFromChannels() = runBlocking(pool) {
    launch(pool) {
        val value = channelC.receive()
        channelD.send(value)
    }
    launch(pool) {
        channelA.send(42)
    }
}

fun main(): Unit= runBlocking(pool) {
    sendToChannels()
    receiveFromChannels()
}

class RunChecker644: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}