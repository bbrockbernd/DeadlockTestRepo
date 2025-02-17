/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":4,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test219
import org.example.altered.test219.RunChecker219.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>) {
    fun sendToChannel(value: Int) {
        runBlocking(pool) {
            channel.send(value)
        }
    }
}

class B {
    fun receiveFromChannel(channel: Channel<Int>): Int = runBlocking(pool) {
        channel.receive()
    }
}

class C {
    fun processValue(value: Int): Int {
        return value * 2
    }
}

class D(val channel: Channel<Int>) {
    fun sendProcessedValue(value: Int, processValue: (Int) -> Int) {
        runBlocking(pool) {
            channel.send(processValue(value))
        }
    }
}

fun startFirstCoroutine(channel: Channel<Int>) {
    val a = A(channel)
    GlobalScope.launch(pool) {
        a.sendToChannel(1)
    }
}

fun startSecondCoroutine(channel: Channel<Int>) {
    val b = B()
    GlobalScope.launch(pool) {
        b.receiveFromChannel(channel)
    }
}

fun startThirdCoroutine(channel: Channel<Int>) {
    val c = C()
    val d = D(channel)
    GlobalScope.launch(pool) {
        d.sendProcessedValue(2, c::processValue)
    }
}

fun startFourthCoroutine(channel: Channel<Int>) {
    val b = B()
    GlobalScope.launch(pool) {
        b.receiveFromChannel(channel)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    startFirstCoroutine(channel)
    startSecondCoroutine(channel)
    startThirdCoroutine(channel)
    startFourthCoroutine(channel)
    Thread.sleep(1000) // Allows coroutines to complete
}

class RunChecker219: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}