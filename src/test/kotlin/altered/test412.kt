/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":2,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test412
import org.example.altered.test412.RunChecker412.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A {
    val channel = Channel<Int>(2)
}

class B(val a: A) {
    val channel = Channel<Int>()
}

class C(val b: B) {
    val channel = Channel<Int>()
}

class D(val c: C) {
    val channel = Channel<Int>()
}

class E(val d: D) {
    val channel = Channel<Int>(2)
}

fun func1(a: A) {
    runBlocking(pool) {
        launch(pool) {
            for (i in 1..5) {
                a.channel.send(i)
            }
        }
    }
}

suspend fun func2(b: B) {
    b.channel.send(b.a.channel.receive())
}

suspend fun func3(c: C) {
    c.channel.send(c.b.channel.receive())
}

suspend fun func4(d: D) {
    d.channel.send(d.c.channel.receive())
}

fun func5(e: E) {
    runBlocking(pool) {
        launch(pool) {
            for (i in 1..5) {
                e.channel.send(e.d.channel.receive())
            }
        }
    }
}

fun coroutine1(e: E) {
    runBlocking(pool) {
        val a = A()
        val b = B(a)
        val c = C(b)
        val d = D(c)
        val e = E(d)
        
        launch(pool) {
            func1(a)
            func2(b)
            func3(c)
            func4(d)
            func5(e)
        }

        for (i in 1..5) {
            println("Received in coroutine1: ${e.channel.receive()}")
        }
    }
}

fun coroutine2(c: C) {
    runBlocking(pool) {
        val a = A()
        val b = B(a)
        val c = C(b)
        
        launch(pool) {
            func1(a)
            func2(b)
            func3(c)
        }

        for (i in 1..5) {
            println("Received in coroutine2: ${c.channel.receive()}")
        }
    }
}

fun main(): Unit{
    val a = A()
    val b = B(a)
    val c = C(b)
    val d = D(c)
    val e = E(d)

    coroutine1(e)
    coroutine2(c)
}

class RunChecker412: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}