/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 7 different coroutines
- 2 different classes

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
package org.example.altered.test235
import org.example.altered.test235.RunChecker235.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val sendChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            sendChannel.send(i)
        }
    }
}

class Consumer(private val receiveChannel: Channel<Int>, private val sendChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val item = receiveChannel.receive()
            sendChannel.send(item)
        }
    }
}

val channelA = Channel<Int>()
val channelB = Channel<Int>()
val channelC = Channel<Int>()
val channelD = Channel<Int>()
val channelE = Channel<Int>()
val channelF = Channel<Int>()
val channelG = Channel<Int>()

fun function1() = runBlocking(pool) {
    val producer1 = Producer(channelA)
    val consumer1 = Consumer(channelA, channelB)
    launch(pool) { producer1.produce() }
    launch(pool) { consumer1.consume() }
}

fun function2() = runBlocking(pool) {
    val producer2 = Producer(channelC)
    val consumer2 = Consumer(channelC, channelD)
    launch(pool) { producer2.produce() }
    launch(pool) { consumer2.consume() }
}

fun function3() = runBlocking(pool) {
    val producer3 = Producer(channelE)
    val consumer3 = Consumer(channelE, channelF)
    launch(pool) { producer3.produce() }
    launch(pool) { consumer3.consume() }
}

fun function4() = runBlocking(pool) {
    val consumer4 = Consumer(channelF, channelG)
    val producer4 = Producer(channelG)
    launch(pool) { consumer4.consume() }
    // This coroutine will lead to a deadlock
    launch(pool) { producer4.produce() }
}

fun main(): Unit {
    function1()
    function2()
    function3()
    function4()
}

class RunChecker235: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}