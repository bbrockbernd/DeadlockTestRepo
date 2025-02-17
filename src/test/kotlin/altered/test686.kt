/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test686
import org.example.altered.test686.RunChecker686.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun processA() {
        val received = inputChannel.receive()
        outputChannel.send(received + 1)
    }
}

class ProcessorB(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun processB() {
        val received = inputChannel.receive()
        outputChannel.send(received * 2)
    }
}

class ProcessorC(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun processC() {
        val received = inputChannel.receive()
        outputChannel.send(received - 3)
    }
}

fun initChannels(): List<Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    return listOf(channel1, channel2, channel3, channel4, channel5)
}

suspend fun runProcessA(processorA: ProcessorA) {
    processorA.processA()
}

suspend fun runProcessB(processorB: ProcessorB) {
    processorB.processB()
}

suspend fun runProcessC(processorC: ProcessorC) {
    processorC.processC()
}

fun main(): Unit= runBlocking(pool) {
    val channels = initChannels()
    
    val processorA = ProcessorA(channels[0], channels[1])
    val processorB = ProcessorB(channels[1], channels[2])
    val processorC = ProcessorC(channels[2], channels[3])
    
    launch(pool) { runProcessA(processorA) }
    launch(pool) { runProcessB(processorB) }
    launch(pool) { runProcessC(processorC) }
    
    coroutineScope {
        launch(pool) { channels[0].send(5) }
        launch(pool) { println("Final result: ${channels[3].receive()}") }
    }
}

class RunChecker686: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}