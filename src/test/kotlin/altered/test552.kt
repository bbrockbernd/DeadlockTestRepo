/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test552
import org.example.altered.test552.RunChecker552.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()
    private val channelC = Channel<Int>()
    private val channelD = Channel<Int>()

    suspend fun processA() {
        val result = channelA.receive()
        channelB.send(result + 1)
    }

    suspend fun processB() {
        val result = channelB.receive()
        channelC.send(result + 1)
    }

    suspend fun processC() {
        val result = channelC.receive()
        channelD.send(result + 1)
    }

    fun runProcess() = runBlocking(pool) {
        launch(pool) {
            channelA.send(0)
            processA()
        }

        launch(pool) {
            processA()
        }

        launch(pool) {
            processB()
        }

        launch(pool) {
            processC()
        }

        launch(pool) {
            channelD.receive() // This will cause a block, leading to deadlock
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.runProcess()
}

class RunChecker552: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}