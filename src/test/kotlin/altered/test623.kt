/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test623
import org.example.altered.test623.RunChecker623.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>) {
    suspend fun sendData() {
        val value = 1
        channel.send(value)
    }
}

class B(val channel: Channel<Int>) {
    suspend fun receiveData() {
        val received = channel.receive()
        println("B received: $received")
    }
}

class C(val channel: Channel<Int>) {
    suspend fun process() {
        val received = channel.receive()
        println("C processed: $received")
        channel.send(received * 2)
    }
}

fun runA(a: A) = runBlocking(pool) {
    a.sendData()
}

fun runB(b: B) = runBlocking(pool) {
    b.receiveData()
}

fun runC(c: C) = runBlocking(pool) {
    c.process()
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    val a = A(channel)
    val b1 = B(channel)
    val c = C(channel)

    launch(pool) {
        runA(a)
    }

    launch(pool) {
        runB(b1)
    }

    launch(pool) {
        runC(c)
    }
}

class RunChecker623: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}