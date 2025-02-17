/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":6,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
- 6 different coroutines
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
package org.example.altered.test46
import org.example.altered.test46.RunChecker46.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit = runBlocking(pool) {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    launch(pool) { functionA(ch1, ch2, ch3) }
    launch(pool) { functionB(ch2, ch4, ch7) }
    launch(pool) { functionA(ch5, ch6, ch7) }
    launch(pool) { functionB(ch1, ch3, ch5) }
    launch(pool) { functionA(ch4, ch5, ch6) }
    launch(pool) { functionB(ch6, ch1, ch3) }
}

suspend fun functionA(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) = coroutineScope {
    launch(pool) { channel2.send(1) }
    launch(pool) { channel3.receive() }
    launch(pool) { channel1.send(1) }
}

suspend fun functionB(channel1: Channel<Int>, channel4: Channel<Int>, channel7: Channel<Int>) = coroutineScope {
    launch(pool) { channel4.send(1) }
    launch(pool) { channel7.receive() }
    launch(pool) { channel1.send(1) }
}

class RunChecker46: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}