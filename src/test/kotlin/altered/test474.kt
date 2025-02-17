/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test474
import org.example.altered.test474.RunChecker474.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class WorkerA {
    val channelA: Channel<Int> = Channel()
    suspend fun sendA(value: Int) = channelA.send(value)
    suspend fun receiveA(): Int = channelA.receive()
}

class WorkerB {
    val channelB: Channel<Int> = Channel()
    suspend fun sendB(value: Int) = channelB.send(value)
    suspend fun receiveB(): Int = channelB.receive()
}

class WorkerC {
    val channelC: Channel<String> = Channel()
    suspend fun sendC(value: String) = channelC.send(value)
    suspend fun receiveC(): String = channelC.receive()
}

class WorkerD {
    val channelD: Channel<String> = Channel()
    suspend fun sendD(value: String) = channelD.send(value)
    suspend fun receiveD(): String = channelD.receive()
}

suspend fun workerAFunc(workerA: WorkerA, workerB: WorkerB) {
    workerA.sendA(1)
    println("WorkerA sent: 1")
    val received = workerB.receiveB()
    println("WorkerA received: $received")
}

suspend fun workerBFunc(workerB: WorkerB, workerC: WorkerC) {
    workerB.sendB(2)
    println("WorkerB sent: 2")
    val received = workerC.receiveC()
    println("WorkerB received: $received")
}

suspend fun workerCFunc(workerC: WorkerC, workerD: WorkerD) {
    workerC.sendC("Hello")
    println("WorkerC sent: Hello")
    val received = workerD.receiveD()
    println("WorkerC received: $received")
}

suspend fun workerDFunc(workerD: WorkerD, workerA: WorkerA) {
    workerD.sendD("World")
    println("WorkerD sent: World")
    val received = workerA.receiveA()
    println("WorkerD received: $received")
}

fun main(): Unit= runBlocking(pool) {
    val workerA = WorkerA()
    val workerB = WorkerB()
    val workerC = WorkerC()
    val workerD = WorkerD()

    launch(pool) { workerAFunc(workerA, workerB) }
    launch(pool) { workerBFunc(workerB, workerC) }
    launch(pool) { workerCFunc(workerC, workerD) }
    launch(pool) { workerDFunc(workerD, workerA) }

    launch(pool) {
        val received = workerA.receiveA()
        println("Main received from WorkerA: $received")
    }

    launch(pool) {
        val received = workerB.receiveB()
        println("Main received from WorkerB: $received")
    }

    launch(pool) {
        val received = workerC.receiveC()
        println("Main received from WorkerC: $received")
    }

    launch(pool) {
        val received = workerD.receiveD()
        println("Main received from WorkerD: $received")
    }
}


class RunChecker474: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}