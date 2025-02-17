/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test554
import org.example.altered.test554.RunChecker554.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit{
    runBlocking(pool) {
        val channel = Channel<Int>()
        launchCoroutineA(channel)
        launchCoroutineB(channel)
    }
}

fun launchCoroutineA(channel: Channel<Int>) {
    GlobalScope.launch(pool) {
        val resultA1 = functionA1(channel)
        val resultA2 = functionA2(channel)
        functionA3(resultA1, resultA2)
    }
}

fun launchCoroutineB(channel: Channel<Int>) {
    GlobalScope.launch(pool) {
        val resultB1 = functionB1(channel)
        val resultB2 = functionB2(channel)
        functionB3(resultB1, resultB2)
    }
}

suspend fun functionA1(channel: Channel<Int>): Int {
    channel.send(1) // Waits for receiver
    return 1
}

suspend fun functionA2(channel: Channel<Int>): Int {
    return channel.receive() // Waits for sender
}

suspend fun functionB1(channel: Channel<Int>): Int {
    channel.send(2) // Waits for receiver
    return 2
}

suspend fun functionB2(channel: Channel<Int>): Int {
    return channel.receive() // Waits for sender
}

fun functionA3(a1: Int, a2: Int): Int {
    return a1 + a2
}

fun functionB3(b1: Int, b2: Int): Int {
    return b1 + b2
}

class RunChecker554: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}