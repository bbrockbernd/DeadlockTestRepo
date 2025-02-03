/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 6 different coroutines
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
package org.example.altered.test383
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A {
    val channelA = Channel<Int>()
    suspend fun functionA() {
        channelA.send(42)
        println("Sent to channelA")
        channelA.receive()
    }
}

class B {
    val channelB = Channel<Int>()
    suspend fun functionB() {
        val value = channelB.receive()
        println("Received from channelB: $value")
    }
}

class C {
    val channelC = Channel<Int>()
    suspend fun functionC(a: A, b: B) {
        channelC.send(43)
        println("Sent to channelC")
        a.channelA.receive()
        b.channelB.send(44)
    }
}

class D {
    val channelD = Channel<Int>()
    suspend fun functionD(c: C, e: E) {
        val value = channelD.receive()
        println("Received from channelD: $value")
        e.channelE.send(45)
    }
}

class E {
    val channelE = Channel<Int>()
    suspend fun functionE(d: D, f: F) {
        val value = channelE.receive()
        println("Received from channelE: $value")
        f.channelF.send(46)
        d.channelD.send(47)
    }
}

class F {
    val channelF = Channel<Int>()
    suspend fun functionF(c: C) {
        val value = channelF.receive()
        println("Received from channelF: $value")
        c.channelC.send(48)
    }
}

fun main(): Unit= runBlocking {
    val a = A()
    val b = B()
    val c = C()
    val d = D()
    val e = E()
    val f = F()

    launch { a.functionA() }
    launch { b.functionB() }
    launch { c.functionC(a, b) }
    launch { d.functionD(c, e) }
    launch { e.functionE(d, f) }
    launch { f.functionF(c) }
}

class RunChecker383: RunCheckerBase() {
    override fun block() = main()
}