/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":3,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test199
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Manager {
    val worker1Channel = Channel<Int>()
    val worker2Channel = Channel<Int>()
    val resultChannel = Channel<Int>()
}

class Worker1(val manager: Manager) {
    suspend fun doWork() {
        repeat(5) {
            delay(100)
            manager.worker1Channel.send(it)
        }
    }
}

class Worker2(val manager: Manager) {
    suspend fun doWork() {
        repeat(5) {
            val value = manager.worker1Channel.receive()
            delay(150)
            manager.worker2Channel.send(value * 2)
        }
    }
}

class Aggregator(val manager: Manager) {
    suspend fun aggregate() {
        repeat(5) {
            val value = manager.worker2Channel.receive()
            manager.resultChannel.send(value + 1)
        }
    }
}

suspend fun start(work1: Worker1, work2: Worker2) {
    coroutineScope {
        launch { work1.doWork() }
        launch { work2.doWork() }
    }
}

suspend fun collectResults(aggregator: Aggregator, manager: Manager) {
    coroutineScope {
        launch { aggregator.aggregate() }
        launch {
            repeat(5) {
                val result = manager.resultChannel.receive()
                println("Result: $result")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val manager = Manager()
    val worker1 = Worker1(manager)
    val worker2 = Worker2(manager)
    val aggregator = Aggregator(manager)

    start(worker1, worker2)
    collectResults(aggregator, manager)
}

class RunChecker199: RunCheckerBase() {
    override fun block() = main()
}