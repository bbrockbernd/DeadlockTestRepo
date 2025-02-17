/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":8,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.altered.test331
import org.example.altered.test331.RunChecker331.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>() // Unbuffered channel
    val channel2 = Channel<Int>() // Unbuffered channel

    launch(pool) { functionA(channel1, channel2) }
    launch(pool) { functionA(channel1, channel2) }
    launch(pool) { functionB(channel1, channel2) }
    launch(pool) { functionB(channel1, channel2) }
    launch(pool) { functionB(channel1, channel2) }
    launch(pool) { functionC(channel1) }
    launch(pool) { functionC(channel2) }
    launch(pool) { functionC(channel2) }

    delay(5000) // Allow time to observe deadlock
}

suspend fun functionA(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.receive()
}

suspend fun functionB(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel2.send(2)
    channel1.receive()
}

suspend fun functionC(channel: Channel<Int>) {
    channel.receive()
}

class RunChecker331: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}