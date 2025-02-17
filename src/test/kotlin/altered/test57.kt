/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 3 different coroutines
- 3 different classes

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
package org.example.altered.test57
import org.example.altered.test57.RunChecker57.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Generator(private val output: Channel<Int>) {
    suspend fun generateValues() {
        for (i in 1..5) {
            output.send(i)
        }
    }
}

class Processor(private val input: Channel<Int>, private val output: Channel<Int>) {
    suspend fun processValues() {
        for (i in 1..5) {
            val value = input.receive()
            output.send(value * 2)
        }
    }
}

class Consumer(private val input1: Channel<Int>, private val input2: Channel<Int>) {
    suspend fun consumeValues() {
        repeat(5) {
            println("Received transformed value: ${input1.receive()}")
            println("Received original value: ${input2.receive()}")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(2)

    val generator = Generator(channel1)
    val processor = Processor(channel1, channel2)
    val consumer = Consumer(channel2, channel3)

    launch(pool) {
        generator.generateValues()
    }

    launch(pool) {
        processor.processValues()
    }

    launch(pool) {
        consumer.consumeValues()
    }

    distributeValues(channel1, channel4)
    finalize(channel3, channel4)
}

suspend fun distributeValues(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        val value = channel1.receive()
        channel2.send(value)
    }
}

suspend fun finalize(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(channel2.receive())
    }
}

class RunChecker57: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}