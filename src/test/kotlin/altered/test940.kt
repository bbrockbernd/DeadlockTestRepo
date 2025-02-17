/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.altered.test940
import org.example.altered.test940.RunChecker940.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerConsumer {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    suspend fun producer1() {
        channel1.send(1)
        channel2.receive()
    }

    suspend fun consumer1() {
        channel2.send(2)
        channel1.receive()
    }
}

class Processor {
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    suspend fun processor1() {
        channel3.send(3)
        channel4.receive()
    }

    suspend fun processor2() {
        channel4.send(4)
        channel3.receive()
    }
}

fun main(): Unit= runBlocking(pool) {
    val pc = ProducerConsumer()
    val pr = Processor()

    launch(pool) { pc.producer1() }
    launch(pool) { pc.consumer1() }
    launch(pool) { pr.processor1() }
    launch(pool) { pr.processor2() }

    delay(1000)
    println("Deadlock detection example completed.")
}

class RunChecker940: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}