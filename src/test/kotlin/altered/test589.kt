/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 2 different coroutines
- 3 different classes

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
- lists, arrays or other datastructures
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
package org.example.altered.test589
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch1: Channel<Int>, val ch2: Channel<Int>)
class B(val ch3: Channel<Int>, val ch4: Channel<Int>)
class C(val ch5: Channel<Int>)

fun f(a: A, b: B) {
    runBlocking {
        launch {
            a.ch1.send(1)
            b.ch3.send(3)
            a.ch2.receive()
            b.ch4.send(4)
        }

        launch {
            a.ch2.send(2)
            b.ch4.receive()
            a.ch1.receive()
            b.ch3.receive()
        }
    }
}

suspend fun g(c: C, a: A, b: B) {
    coroutineScope {
        launch {
            c.ch5.send(5)
            a.ch2.receive()
            b.ch3.receive()
        }

        launch {
            c.ch5.receive()
            a.ch1.send(6)
            b.ch4.send(7)
        }
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
    val c = C(ch5)

    runBlocking {
        launch {
            f(a, b)
        }

        launch {
            g(c, a, b)
        }
    }
}

class RunChecker589: RunCheckerBase() {
    override fun block() = main()
}