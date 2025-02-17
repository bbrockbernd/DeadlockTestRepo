/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test39
import org.example.altered.test39.RunChecker39.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class WorkerChannel(val channel: Channel<Int>)

suspend fun producer(channel: Channel<Int>) {
    repeat(5) {
        println("Producing $it")
        channel.send(it)
    }
}

suspend fun consumer(channel: Channel<Int>) {
    repeat(5) {
        val received = channel.receive()
        println("Consuming $received")
    }
}

fun processChannel(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            producer(channel)
        }

        launch(pool) {
            consumer(channel)
        }
    }
}

fun main(): Unit{
    runBlocking(pool) {
        val channel = Channel<Int>()
        val workerChannel = WorkerChannel(channel)

        repeat(2) {
            launch(pool) {
                processChannel(workerChannel.channel)
            }
        }

        launch(pool) {
            processChannel(workerChannel.channel)
        }

        launch(pool) {
            processChannel(workerChannel.channel)
        }

        launch(pool) {
            processChannel(workerChannel.channel)
        }
    }
}

class RunChecker39: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}