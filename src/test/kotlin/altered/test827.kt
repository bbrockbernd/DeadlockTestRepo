/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.altered.test827
import org.example.altered.test827.RunChecker827.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    coroutineScope {
        launch(pool) { firstFunction(channel1, channel2) }
        launch(pool) { secondFunction(channel2, channel3) }
        launch(pool) { thirdFunction(channel3, channel1) }
        launch(pool) { deadlockFunction(channel1, channel2, channel3) }
    }
}

suspend fun firstFunction(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.receive()
}

suspend fun secondFunction(channel2: Channel<Int>, channel3: Channel<Int>) {
    channel2.send(2)
    channel3.receive()
}

suspend fun thirdFunction(channel3: Channel<Int>, channel1: Channel<Int>) {
    channel3.send(3)
    channel1.receive()
}

suspend fun deadlockFunction(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    channel1.receive()
    channel2.receive()
    channel3.receive()
}

class RunChecker827: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}