/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":3,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.altered.test130
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// ----------------- Classes -----------------

class A(val ch1: Channel<String>, val ch2: Channel<String>)
class B(val ch1: Channel<Int>, val ch2: Channel<Int>)
class C(val ch1: Channel<Double>, val ch2: Channel<Double>)
class D(val ch1: Channel<String>, val ch2: Channel<Int>)
class E(val ch1: Channel<Int>, val ch2: Channel<Double>)

// ----------------- Functions -----------------

suspend fun func1(a: A) {
    a.ch1.send("Hello")
    val msg = a.ch2.receive()
}

suspend fun func2(b: B) {
    b.ch1.send(42)
    val num = b.ch2.receive()
}

suspend fun func3(c: C) {
    c.ch1.send(3.14)
    val num = c.ch2.receive()
}

suspend fun func4(d: D) {
    val msg = d.ch1.receive()
    func5(d.ch1, d.ch2)
}

suspend fun func5(ch1: Channel<String>, ch2: Channel<Int>) {
    ch1.send("World")
    ch2.send(100)
}

suspend fun func6(e: E, a: A) {
    val num = e.ch1.receive()
    func7(e.ch2)
    a.ch2.send("Hi")
}

suspend fun func7(ch: Channel<Double>) {
    val num = ch.receive()
    ch.send(6.28)
}

// ----------------- Main Coroutine Setup -----------------

fun main(): Unit= runBlocking {
    val ch1 = Channel<String>()
    val ch2 = Channel<String>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Double>()
    val ch6 = Channel<Double>()
    val ch7 = Channel<String>()
    val ch8 = Channel<Int>()

    val a = A(ch1, ch2)
    val b = B(ch3, ch4)
    val c = C(ch5, ch6)
    val d = D(ch7, ch8)
    val e = E(ch8, ch5)

    launch { func1(a) }
    launch { func2(b) }
    launch { func6(e, a) }
    
    func3(c)
    func4(d)
}

class RunChecker130: RunCheckerBase() {
    override fun block() = main()
}