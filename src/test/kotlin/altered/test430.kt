/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 6 different coroutines
- 5 different classes

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
package org.example.altered.test430
import org.example.altered.test430.RunChecker430.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch: Channel<Int>)
class B(val ch: Channel<Int>)
class C(val ch: Channel<Int>)
class D(val ch: Channel<Int>)
class E(val ch: Channel<Int>)
class F(val ch: Channel<Int>)

fun function1(a: A, b: B) = runBlocking(pool) {
    launch(pool) {
        a.ch.send(1)
        val received = b.ch.receive()
    }
}

fun function2(c: C, d: D) = runBlocking(pool) {
    launch(pool) {
        c.ch.send(2)
        val received = d.ch.receive()
    }
}

fun function3(e: E, f: F) = runBlocking(pool) {
    launch(pool) {
        e.ch.send(3)
        val received = f.ch.receive()
    }
}

fun function4(a: A, e: E) = runBlocking(pool) {
    launch(pool) {
        e.ch.send(a.ch.receive())
    }
}

fun function5(b: B, f: F) = runBlocking(pool) {
    launch(pool) {
        f.ch.send(b.ch.receive())
    }
}

fun function6(c: C, d: D) = runBlocking(pool) {
    launch(pool) {
        val value = c.ch.receive()
        d.ch.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val a = A(Channel<Int>())
    val b = B(Channel<Int>())
    val c = C(Channel<Int>())
    val d = D(Channel<Int>(1))
    val e = E(Channel<Int>())
    val f = F(Channel<Int>())

    function1(a, b)
    function2(c, d)
    function3(e, f)
    function4(a, e)
    function5(b, f)
    function6(c, d)
}

class RunChecker430: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}