/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":2,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
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
package org.example.altered.test458
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>)
class B(val channel2: Channel<Int>)
class C(val channel3: Channel<Int>)
class D(val channel4: Channel<Int>)
class E(val channel5: Channel<Int>)

class F(val a: A, val b: B, val c: C, val d: D)
class G(val a: A, val b: B, val e: E)
class H(val c: C, val d: D)
class I(val e: E)

fun function1(a: A, b: B) {
    runBlocking {
        a.channel1.send(1)
        b.channel2.receive()
    }
}

fun function2(b: B, c: C) {
    runBlocking {
        b.channel2.send(2)
        c.channel3.receive()
    }
}

fun function3(c: C, d: D) {
    runBlocking {
        c.channel3.send(3)
        d.channel4.receive()
    }
}

fun function4(d: D, e: E) {
    runBlocking {
        d.channel4.send(4)
        e.channel5.receive()
    }
}

fun function5(e: E, a: A) {
    runBlocking {
        e.channel5.send(5)
        a.channel1.receive()
    }
}

fun function6(f: F) {
    function1(f.a, f.b)
    function2(f.b, f.c)
}

fun function7(g: G) {
    function3(g.a, g.e)
    function4(g.b, g.a)
}

fun function8(h: H, i: I) {
    function5(i.e, h.c)
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val a = A(channel1)
    val b = B(channel2)
    val c = C(channel3)
    val d = D(channel4)
    val e = E(channel5)

    val f = F(a, b, c, d)
    val g = G(a, b, e)
    val h = H(c, d)
    val i = I(e)

    runBlocking {
        launch {
            function6(f)
        }
        launch {
            function7(g)
            function8(h, i)
        }
    }
}

class RunChecker458: RunCheckerBase() {
    override fun block() = main()
}