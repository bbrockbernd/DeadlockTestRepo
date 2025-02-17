/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
- 1 different classes

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
package org.example.altered.test419
import org.example.altered.test419.RunChecker419.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

//Class with a channel property
class ChannelWrapper(val channel: Channel<Int>)

// Function 1: Initializes channels and starts coroutines
fun initialize() {
    val channelOne = Channel<Int>()
    val channelTwo = Channel<Int>()
    val channelThree = Channel<Int>()
    val channelFour = Channel<Int>()

    val wrapperOne = ChannelWrapper(channelOne)
    val wrapperTwo = ChannelWrapper(channelTwo)

    runBlocking(pool) {
        launch(pool) { coroutineOne(wrapperOne.channel, wrapperTwo.channel) }
        launch(pool) { coroutineTwo(wrapperTwo.channel, channelThree) }
        launch(pool) { coroutineThree(channelThree, channelFour) }
        launch(pool) { coroutineFour(channelFour, wrapperOne.channel) }
    }
}

// Function 2: Coroutine one sends and receives from channels
suspend fun coroutineOne(channelOne: Channel<Int>, channelTwo: Channel<Int>) {
    for (i in 1..5) {
        channelOne.send(i)
        val result = channelTwo.receive()
        println("Coroutine One received: $result")
    }
}

// Function 3: Coroutine two sends and receives from channels
suspend fun coroutineTwo(channelOne: Channel<Int>, channelTwo: Channel<Int>) {
    for (i in 1..5) {
        val result = channelOne.receive()
        channelTwo.send(result * 2)
        println("Coroutine Two sent: ${result * 2}")
    }
}

// Function 4: Coroutine three sends and receives from channels
suspend fun coroutineThree(channelOne: Channel<Int>, channelTwo: Channel<Int>) {
    for (i in 1..5) {
        val result = channelOne.receive()
        channelTwo.send(result + 3)
        println("Coroutine Three sent: ${result + 3}")
    }
}

// Function 5: Coroutine four sends and receives from channels
suspend fun coroutineFour(channelOne: Channel<Int>, channelTwo: Channel<Int>) {
    for (i in 1..5) {
        val result = channelOne.receive()
        channelTwo.send(result - 1)
        println("Coroutine Four sent: ${result - 1}")
    }
}

// Entry point
fun main(): Unit {
    initialize()
}

class RunChecker419: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}