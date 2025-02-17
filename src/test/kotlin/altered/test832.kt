/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test832
import org.example.altered.test832.RunChecker832.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
}

fun sendToChannelA(processor: Processor) {
    runBlocking(pool) {
        processor.channelA.send(1)
        processor.channelB.receive()
    }
}

fun sendToChannelB(processor: Processor) {
    runBlocking(pool) {
        processor.channelB.send(2)
        processor.channelA.receive()
    }
}

fun startProcess(processor: Processor) {
    runBlocking(pool) {
        launch(pool) {
            sendToChannelA(processor)
        }
        launch(pool) {
            sendToChannelB(processor)
        }
    }
}

fun initiatePipeline() {
    val processor = Processor()
    startProcess(processor)
}

fun main(): Unit{
    initiatePipeline()
}

class RunChecker832: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}