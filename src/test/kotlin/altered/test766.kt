/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test766
import org.example.altered.test766.RunChecker766.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    channel1.send(1)  // Waits for receiver
    val received = channel2.receive() 
    println("Function1 received: $received")
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking(pool) {
    val received = channel2.receive()
    println("Function2 received: $received")
    channel3.send(2)  // Waits for receiver
}

fun function3(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking(pool) {
    val received = channel3.receive()
    println("Function3 received: $received")
    channel4.send(3)  // Waits for receiver
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch(pool) {
        function1(channel1, channel2)
    }
    launch(pool) {
        function2(channel2, channel3)
    }
    launch(pool) {
        function3(channel3, channel4)
    }

    val received = channel4.receive()  // Waits for receiver
    println("Main received: $received")
}

class RunChecker766: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}