/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
- 7 different coroutines
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
package org.example.altered.test351
import org.example.altered.test351.RunChecker351.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val data = input.receive()
            output.send(data * 2)
        }
    }
}

class Manager(private val worker1: Worker, private val worker2: Worker, private val result: Channel<Int>) {
    suspend fun manage() {
        repeat(5) {
            val data1 = worker1.output.receive()
            val data2 = worker2.output.receive()
            result.send(data1 + data2)
        }
    }
}

fun processInput1(input1: Channel<Int>, output: Channel<Int>) = runBlocking(pool) {
    repeat(5) {
        input1.send(it)
    }
}

fun processInput2(input2: Channel<Int>, output: Channel<Int>) = runBlocking(pool) {
    repeat(5) {
        input2.send(it + 10)
    }
}

fun startWorker1(input1: Channel<Int>, output: Channel<Int>) = runBlocking(pool) {
    val worker1 = Worker(input1, output)
    launch(pool) {
        worker1.process()
    }
}

fun startWorker2(input2: Channel<Int>, output: Channel<Int>) = runBlocking(pool) {
    val worker2 = Worker(input2, output)
    launch(pool) {
        worker2.process()
    }
}

fun startManager(worker1: Worker, worker2: Worker, result: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val manager = Manager(worker1, worker2, result)
        manager.manage()
    }
}

fun main(): Unit = runBlocking(pool) {
    val input1 = Channel<Int>()
    val input2 = Channel<Int>()
    val worker1Output = Channel<Int>()
    val worker2Output = Channel<Int>()
    val result = Channel<Int>()

    launch(pool) { processInput1(input1, worker1Output) }
    launch(pool) { processInput2(input2, worker2Output) }
    launch(pool) { startWorker1(input1, worker1Output) }
    launch(pool) { startWorker2(input2, worker2Output) }
    launch(pool) { 
        val worker1 = Worker(input1, worker1Output) 
        val worker2 = Worker(input2, worker2Output)
        startManager(worker1, worker2, result)
    }

    launch(pool) {
        repeat(5) {
            println(result.receive())
        }
    }
}

class RunChecker351: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}