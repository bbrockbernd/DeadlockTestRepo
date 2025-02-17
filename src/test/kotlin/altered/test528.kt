/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
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
package org.example.altered.test528
import org.example.altered.test528.RunChecker528.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class WorkerA(private val channel: Channel<String>) {
    suspend fun sendMessages() {
        channel.send("Message 1 from WorkerA")
        channel.send("Message 2 from WorkerA")
    }
}

class WorkerB(private val channel: Channel<String>) {
    suspend fun receiveMessages() {
        val message1 = channel.receive()
        val message2 = channel.receive()
        println(message1)
        println(message2)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<String>()
    
    val workerA = WorkerA(channel)
    val workerB = WorkerB(channel)

    launch(pool) {
        workerA.sendMessages()
    }

    launch(pool) {
        workerB.receiveMessages()
    }

    launch(pool) {
        workerA.sendMessages()
    }

    launch(pool) {
        workerB.receiveMessages()
    }
}

class RunChecker528: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}