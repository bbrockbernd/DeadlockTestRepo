/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
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
package org.example.altered.test765
import org.example.altered.test765.RunChecker765.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun produceNumbers(channel: SendChannel<Int>) = runBlocking(pool) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

suspend fun doubleNumbers(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
    for (n in inputChannel) {
        outputChannel.send(n * 2)
    }
    outputChannel.close()
}

suspend fun sumNumbers(channel1: ReceiveChannel<Int>, channel2: ReceiveChannel<Int>, resultChannel: SendChannel<Int>) {
    var sum = 0
    for (n in channel1) {
        sum += n
    }
    for (n in channel2) {
        sum += n
    }
    resultChannel.send(sum)
    resultChannel.close()
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val resultChannel = Channel<Int>()

    launch(pool) { produceNumbers(channel1) }
    launch(pool) { doubleNumbers(channel1, channel2) }
    launch(pool) { sumNumbers(channel1, channel2, resultChannel) }

    val result = resultChannel.receive()
    println("Result: $result")
}

class RunChecker765: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}