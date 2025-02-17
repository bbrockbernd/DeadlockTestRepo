/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test597
import org.example.altered.test597.RunChecker597.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun functionA(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val value = channel1.receive()
        channel2.send(value)
    }
}

fun functionB(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val value = channel2.receive()
        channel3.send(value)
    }
}

fun functionC(channel3: Channel<Int>, channel1: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val value = channel3.receive()
        channel1.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch(pool) { functionA(channel1, channel2) }
    launch(pool) { functionB(channel2, channel3) }
    launch(pool) { functionC(channel3, channel1) }

    channel1.send(42) // Initial value to kickstart the chain.
}

class RunChecker597: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}