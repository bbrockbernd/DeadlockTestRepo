/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
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
package org.example.altered.test0
import org.example.altered.test0.RunChecker0.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>(1) // buffered channel
val channel5 = Channel<Int>(1) // buffered channel
val channel6 = Channel<Int>()

fun functionA() = runBlocking(pool) {
    channel1.send(1)
    channel3.receive()
    channel4.send(4)
}

fun functionB() = runBlocking(pool) {
    channel2.send(2)
    channel5.receive()
    channel1.receive() // This will never be received due to the cycle
}

fun functionC() = runBlocking(pool) {
    channel4.receive()
    channel6.send(6)
    channel2.receive()
}

fun functionD() = runBlocking(pool) {
    channel5.send(5)
    channel3.send(3)
    channel6.receive()
}

fun main(): Unit = runBlocking(pool) {
    launch(pool) { functionA() }
    launch(pool) { functionB() }
    launch(pool) { functionC() }
    launch(pool) { functionD() }
}

class RunChecker0: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}