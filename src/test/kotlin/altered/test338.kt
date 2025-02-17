/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 8 different coroutines
- 4 different classes

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
package org.example.altered.test338
import org.example.altered.test338.RunChecker338.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val received = inputChannel.receive()
            outputChannel.send(received * 2)
        }
    }
}

class SecondClass(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun compute() {
        for (i in 1..5) {
            val received = inputChannel.receive()
            outputChannel.send(received + 3)
        }
    }
}

class ThirdClass(private val inputChannel: Channel<Int>) {
    suspend fun aggregate() {
        for (i in 1..5) {
            val received = inputChannel.receive()
            println("Aggregated value: $received")
        }
    }
}

class FourthClass(private val outputChannel: Channel<Int>) {
    suspend fun generateValues() {
        for (i in 1..5) {
            outputChannel.send(i)
        }
    }
}

fun setupChannels() = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    launch(pool) { FourthClass(channel1).generateValues() }
    launch(pool) { FirstClass(channel1, channel2).process() }
    launch(pool) { SecondClass(channel2, channel3).compute() }
    launch(pool) { ThirdClass(channel3).aggregate() }
    launch(pool) { FourthClass(channel4).generateValues() }
    launch(pool) { FirstClass(channel4, channel2).process() }
    launch(pool) { SecondClass(channel2, channel3).compute() }
    launch(pool) { ThirdClass(channel3).aggregate() }
}

fun main(): Unit{
    setupChannels()
}

class RunChecker338: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}