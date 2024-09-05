/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":1,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 1 different coroutines
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
package org.example.generated.test146
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(private val ch1: Channel<Int>, private val ch2: Channel<Int>) {
    suspend fun f1() {
        ch1.send(1)
        ch2.receive()
    }
}

class B(private val ch1: Channel<Int>) {
    suspend fun f2() {
        ch1.receive()
    }
}

class C(private val ch3: Channel<Int>, private val ch4: Channel<Int>) {
    suspend fun f3() {
        ch3.send(3)
        ch4.receive()
    }

    suspend fun f4() {
        ch4.send(4)
        ch3.receive()
    }
}

class D(private val ch5: Channel<Int>, private val ch6: Channel<Int>) {
    suspend fun f5() {
        ch5.send(5)
    }

    suspend fun f6() {
        ch6.receive()
    }
}

suspend fun f7(ch7: Channel<Int>, ch8: Channel<Int>) {
    ch7.send(7)
    ch8.receive()
}

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val a = A(ch1, ch2)
    val b = B(ch1)
    val c = C(ch3, ch4)
    val d = D(ch5, ch6)

    launch {
        a.f1()
        b.f2()
        c.f3()
        c.f4()
        d.f5()
        d.f6()
        f7(ch7, ch8)
    }
}