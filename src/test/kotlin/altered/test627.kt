/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test627
import org.example.altered.test627.RunChecker627.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun process() {
        repeat(5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
    }
}

class Printer(val channel3: Channel<Int>) {
    suspend fun print() {
        repeat(5) {
            val value = channel3.receive()
            println("Received: $value")
        }
    }
}

class Coordinator(private val channel4: Channel<Int>, private val channel5: Channel<Int>) {
    suspend fun coordinate() {
        repeat(5) {
            val value = channel4.receive()
            channel5.send(value + 1)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>(Channel.UNLIMITED)
    val channel2 = Channel<Int>(Channel.UNLIMITED)
    val channel3 = Channel<Int>(Channel.UNLIMITED)
    val channel4 = Channel<Int>(Channel.UNLIMITED)
    val channel5 = Channel<Int>(Channel.UNLIMITED)

    val processor = Processor(channel1, channel2)
    val printer = Printer(channel3)
    val coordinator = Coordinator(channel4, channel5)

    launch(pool) {
        repeat(5) {
            channel1.send(it)
        }
    }

    launch(pool) {
        processor.process()
        repeat(5) {
            val value = channel2.receive()
            channel4.send(value)
            channel3.send(value * 3)
        }
        printer.print()
    }

    coroutineScope {
        coordinator.coordinate()
        repeat(5) {
            val value = channel5.receive()
            println("Final Value: $value")
        }
    }
}

class RunChecker627: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}