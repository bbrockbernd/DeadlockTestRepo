/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":4,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.altered.test794
import org.example.altered.test794.RunChecker794.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun process() {
        coroutineScope {
            launch(pool) {
                val result = channelA.receive()
                channelB.send(result * 2)
            }
        }
    }
}

class ProcessorB(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun process() {
        coroutineScope {
            launch(pool) {
                val result = channelB.receive()
                channelA.send(result + 3)
            }
        }
    }
}

class ProcessorC(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun process() {
        coroutineScope {
            launch(pool) {
                val result = channelA.receive()
                channelB.send(result - 1)
            }
        }
    }
}

fun runTest(processorA: ProcessorA, processorB: ProcessorB, processorC: ProcessorC) = runBlocking(pool) {
    val jobA = launch(pool) { processorA.process() }
    val jobB = launch(pool) { processorB.process() }
    val jobC = launch(pool) { processorC.process() }

    jobA.join()
    jobB.join()
    jobC.join()
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val processorA = ProcessorA(channelA, channelB)
    val processorB = ProcessorB(channelA, channelB)
    val processorC = ProcessorC(channelA, channelB)

    runTest(processorA, processorB, processorC)
}

class RunChecker794: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}