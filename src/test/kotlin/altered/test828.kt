/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test828
import org.example.altered.test828.RunChecker828.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun nonBlockingTask(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    GlobalScope.launch(pool) {
        val data = channel1.receive()
        channel2.send(data + 1)
        channel3.send(data * 2)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch(pool) {
        // Send initial data
        channel1.send(4)
        println("Received by channel2: ${channel2.receive()}")
        println("Received by channel3: ${channel3.receive()}")
    }

    nonBlockingTask(channel1, channel2, channel3)

    delay(1000L) // Give some time to the coroutine to execute before main finishes
}

class RunChecker828: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}