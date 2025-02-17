/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test943
import org.example.altered.test943.RunChecker943.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        println("Coroutine 1: Sending to channel1")
        channel1.send(1)
        println("Coroutine 1: Receiving from channel2")
        channel2.receive()
    }
}

fun function2(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    coroutineScope {
        launch(pool) {
            println("Coroutine 2: Sending to channel2")
            channel2.send(2)
            println("Coroutine 2: Receiving from channel1")
            channel1.receive()
        }
    }
}

fun function3(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    channel2.receive()
    println("Function3: Received from channel2")
}

fun function4(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    channel1.send(3)
    println("Function4: Sending to channel1")
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel1, channel2) }
    delay(1000) // allow some time for the deadlock to manifest

    println("Testing if we reached this point, if we did, no deadlock, else deadlock occurred.")
}

class RunChecker943: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}