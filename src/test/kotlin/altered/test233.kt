/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":8,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 8 different coroutines
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
package org.example.altered.test233
import org.example.altered.test233.RunChecker233.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch1: Channel<Int>, val ch2: Channel<Int>)

class B(val ch3: Channel<Int>, val ch4: Channel<Int>)

class C(val ch1: Channel<Int>, val ch5: Channel<Int>)

class D(val ch2: Channel<Int>)

fun func1(a: A, b: B) = runBlocking(pool) {
    launch(pool) {
        val value = a.ch1.receive()
        b.ch3.send(value)
    }
}

fun func2(a: A, d: D) = runBlocking(pool) {
    launch(pool) {
        val value = d.ch2.receive()
        a.ch2.send(value)
    }
}

fun func3(b: B, c: C) = runBlocking(pool) {
    launch(pool) {
        val value = b.ch4.receive()
        c.ch5.send(value)
    }
}

fun func4(b: B, d: D) = runBlocking(pool) {
    launch(pool) {
        val value = b.ch3.receive()
        d.ch2.send(value)
    }
}

fun func5(c: C, d: D) = runBlocking(pool) {
    launch(pool) {
        val value = c.ch1.receive()
        d.ch2.send(value)
    }
}

fun func6(a: A, c: C) = runBlocking(pool) {
    launch(pool) {
        val value = a.ch2.receive()
        c.ch1.send(value)
    }
}

fun func7(a: A, b: B, c: C, d: D) = runBlocking(pool) {
    launch(pool) {
        a.ch1.send(1)
        func1(a, b)
        func2(a, d)
        func3(b, c)
        func4(b, d)
        func5(c, d)
        func6(a, c)
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val a = A(ch1, ch2)
    val b = B(ch3, ch4)
    val c = C(ch1, ch5)
    val d = D(ch2)

    func7(a, b, c, d)
}

class RunChecker233: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}