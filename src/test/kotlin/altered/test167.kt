/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test167
import org.example.altered.test167.RunChecker167.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch: Channel<Int>)
class B(val ch: Channel<Int>)
class C(val ch: Channel<Int>)
class D(val ch: Channel<Int>)

fun produceA(a: A) {
    runBlocking(pool) {
        launch(pool) {
            a.ch.send(1)
            println("A sent data")
        }
    }
}

fun produceB(b: B) {
    runBlocking(pool) {
        launch(pool) {
            val data = b.ch.receive()
            println("B received data: $data")
        }
    }
}

fun produceC(c: C, d: D) {
    runBlocking(pool) {
        launch(pool) {
            c.ch.send(2)
            println("C sent data")
        }
        launch(pool) {
            d.ch.receive()
            println("D blocking")
        }
    }
}

fun main(): Unit {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val a = A(channel1)
    val b = B(channel2)
    val c = C(channel3)
    val d = D(channel4)

    runBlocking(pool) {
        launch(pool) { produceA(a) }
        launch(pool) { produceB(b) }
        launch(pool) { produceC(c, d) }
    }
}

class RunChecker167: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}