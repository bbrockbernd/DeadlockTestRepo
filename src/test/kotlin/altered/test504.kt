/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test504
import org.example.altered.test504.RunChecker504.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()

fun sendToChannel1() = runBlocking(pool) {
    channel1.send(1)
    val received = channel3.receive()
    println("Received from channel3 in sendToChannel1: $received")
}

fun sendToChannel2() = runBlocking(pool) {
    val received = channel1.receive()
    println("Received from channel1 in sendToChannel2: $received")
    channel2.send(2)
}

fun sendToChannel3() = runBlocking(pool) {
    val received = channel2.receive()
    println("Received from channel2 in sendToChannel3: $received")
    channel3.send(3)
}

fun startCoroutines() = runBlocking(pool) {
    launch(pool) { sendToChannel1() }
    launch(pool) { sendToChannel2() }
    launch(pool) { sendToChannel3() }
}

fun main(): Unit= runBlocking(pool) {
    startCoroutines()
}

class RunChecker504: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}