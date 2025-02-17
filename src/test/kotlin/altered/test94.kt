/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
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
package org.example.altered.test94
import org.example.altered.test94.RunChecker94.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    launch(pool) {
        coroutineOne(channel1, channel2, channel3, channel4, channel5, channel6)
    }

    launch(pool) {
        coroutineTwo(channel1, channel2, channel3, channel4, channel5, channel6)
    }
}

suspend fun coroutineOne(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>, channel6: Channel<Int>) {
    channel1.send(1)
    channel2.send(channel1.receive())
    channel4.send(channel3.receive())
    channel6.send(channel5.receive())
}

suspend fun coroutineTwo(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>, channel6: Channel<Int>) {
    channel3.send(channel4.receive())
    channel5.send(channel6.receive())
    channel1.send(channel2.receive())
    channel2.send(2)
}

class RunChecker94: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}