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
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch: Channel<Int>)
class B(val ch: Channel<Int>)
class C(val ch: Channel<Int>)
class D(val ch: Channel<Int>)

fun produceA(a: A) {
    runBlocking {
        launch {
            a.ch.send(1)
            println("A sent data")
        }
    }
}

fun produceB(b: B) {
    runBlocking {
        launch {
            val data = b.ch.receive()
            println("B received data: $data")
        }
    }
}

fun produceC(c: C, d: D) {
    runBlocking {
        launch {
            c.ch.send(2)
            println("C sent data")
        }
        launch {
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

    runBlocking {
        launch { produceA(a) }
        launch { produceB(b) }
        launch { produceC(c, d) }
    }
}

class RunChecker167: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}