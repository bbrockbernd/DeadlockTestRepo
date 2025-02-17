/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test919
import org.example.altered.test919.RunChecker919.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch(pool) { coroutine1(channel1, channel2) }
    launch(pool) { coroutine2(channel2, channel3) }
    launch(pool) { coroutine3(channel3, channel4) }
    launch(pool) { coroutine4(channel4, channel5, channel1) }
}

suspend fun coroutine1(ch1: Channel<Int>, ch2: Channel<Int>) {
    ch1.send(1)
    ch2.receive()
}

suspend fun coroutine2(ch2: Channel<Int>, ch3: Channel<Int>) {
    ch3.send(2)
    ch2.receive()
}

suspend fun coroutine3(ch3: Channel<Int>, ch4: Channel<Int>) {
    ch4.send(3)
    ch3.receive()
}

suspend fun coroutine4(ch4: Channel<Int>, ch5: Channel<Int>, ch1: Channel<Int>) {
    ch1.receive()
    ch5.send(4)
}

class RunChecker919: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}