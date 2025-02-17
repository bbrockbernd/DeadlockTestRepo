/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test356
import org.example.altered.test356.RunChecker356.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>(Channel.UNLIMITED)
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(Channel.UNLIMITED)
    val channel4 = Channel<Int>()

    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel2, channel3) }
    launch(pool) { function3(channel3, channel4) }
    launch(pool) { function4(channel4, channel1) }
    launch(pool) { function5(channel1) }
    launch(pool) { function6(channel2) }
    launch(pool) { function6(channel4) }
}

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    val value = channel1.receive()
    channel2.send(value)
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking(pool) {
    val value = channel2.receive()
    channel3.send(value)
}

fun function3(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking(pool) {
    val value = channel3.receive()
    channel4.send(value)
}

fun function4(channel4: Channel<Int>, channel1: Channel<Int>) = runBlocking(pool) {
    val value = channel4.receive()
    channel1.send(value)
}

fun function5(channel: Channel<Int>) = runBlocking(pool) {
    // Some work, no sends or receives
}

fun function6(channel: Channel<Int>) = runBlocking(pool) {
    // Some work, no sends or receives
}

class RunChecker356: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}