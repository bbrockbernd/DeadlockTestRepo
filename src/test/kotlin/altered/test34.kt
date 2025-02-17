/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":6,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 6 different coroutines
- 5 different classes

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
package org.example.altered.test34
import org.example.altered.test34.RunChecker34.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val input: Channel<Int>, val output: Channel<String>)
class ProcessorB(val input: Channel<String>, val output: Channel<Float>)
class ProcessorC(val input: Channel<Float>, val output: Channel<Boolean>)
class ProcessorD(val input: Channel<Boolean>)
class Utility(val flag: Boolean = true)

val channel1 = Channel<Int>()
val channel2 = Channel<String>()
val channel3 = Channel<Float>()
val channel4 = Channel<Boolean>()
val channel5 = Channel<Int>()
val channel6 = Channel<String>()

fun startProcessorA(processor: ProcessorA) {
    GlobalScope.launch(pool) {
        for (message in processor.input) {
            processor.output.send("Processed: $message")
        }
    }
}

fun startProcessorB(processor: ProcessorB) {
    GlobalScope.launch(pool) {
        for (message in processor.input) {
            processor.output.send(message.length.toFloat())
        }
    }
}

fun startProcessorC(processor: ProcessorC) {
    GlobalScope.launch(pool) {
        for (message in processor.input) {
            processor.output.send(message > 10.0)
        }
    }
}

fun startProcessorD(processor: ProcessorD) {
    GlobalScope.launch(pool) {
        for (message in processor.input) {
            println("Final output: $message")
        }
    }
}

fun generateData(channel: Channel<Int>) {
    GlobalScope.launch(pool) {
        repeat(10) { 
            channel.send(it)
        }
    }
}

fun processExtra(channelIn: Channel<Int>, channelOut: Channel<String>) {
    GlobalScope.launch(pool) {
        for (message in channelIn) {
            channelOut.send("Extra: $message")
        }
    }
}

fun initializeProcessors() {
    val processorA = ProcessorA(channel1, channel2)
    val processorB = ProcessorB(channel2, channel3)
    val processorC = ProcessorC(channel3, channel4)
    val processorD = ProcessorD(channel4)
    val utility = Utility()

    startProcessorA(processorA)
    startProcessorB(processorB)
    startProcessorC(processorC)
    startProcessorD(processorD)
}

fun main(): Unit= runBlocking(pool) {
    initializeProcessors()
    generateData(channel1)
    processExtra(channel5, channel6)
}

class RunChecker34: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}