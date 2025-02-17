/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":6,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 6 different coroutines
- 0 different classes

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
package org.example.altered.test210
import org.example.altered.test210.RunChecker210.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun func1(chan1: Channel<Int>, chan2: Channel<Int>, chan3: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val received1 = chan1.receive()
        val received2 = chan2.receive()
        chan3.send(received1 + received2)
    }
}

fun func2(chan3: Channel<Int>, chan4: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val sum = chan3.receive()
        chan4.send(sum * 2)
    }
}

fun func3(chan4: Channel<Int>, chan5: Channel<Int>, chan6: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val doubledSum = chan4.receive()
        chan5.send(doubledSum / 2)
        chan6.send(doubledSum / 4)
    }
}

fun func4(chan5: Channel<Int>, chan6: Channel<Int>, chan7: Channel<Int>, chan8: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val half = chan5.receive()
        val quarter = chan6.receive()
        chan7.send(half)
        chan8.send(quarter)
    }
}

fun main(): Unit= runBlocking(pool) {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()
    val chan6 = Channel<Int>()
    val chan7 = Channel<Int>()
    val chan8 = Channel<Int>()

    launch(pool) {
        chan1.send(10)
    }

    launch(pool) {
        chan2.send(20)
    }

    func1(chan1, chan2, chan3)
    func2(chan3, chan4)
    func3(chan4, chan5, chan6)
    func4(chan5, chan6, chan7, chan8)

    launch(pool) {
        println("chan7: ${chan7.receive()}")
    }

    launch(pool) {
        println("chan8: ${chan8.receive()}")
    }
}

class RunChecker210: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}