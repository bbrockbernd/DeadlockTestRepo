/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":8,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 8 different coroutines
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
package org.example.generated.test379
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class A(val ch1: SendChannel<Int>)
class B(val ch2: SendChannel<Int>)
class C(val ch3: ReceiveChannel<Int>)
class D(val ch4: ReceiveChannel<Int>)
class E(val ch5: ReceiveChannel<Int>)

fun func1(a: A, b: B) {
    runBlocking {
        launch {
            for (i in 1..3) {
                a.ch1.send(i)
            }
        }
        launch {
            for (j in 1..3) {
                b.ch2.send(j)
            }
        }
    }
}

fun func2(c: C, e: E) {
    runBlocking {
        launch {
            for (i in 1..3) {
                println(c.ch3.receive())
            }
        }
        launch {
            for (i in 1..3) {
                println(e.ch5.receive())
            }
        }
    }
}

fun func3(a: A, d: D) {
    runBlocking {
        launch {
            for (i in 1..3) {
                val received = d.ch4.receive()
                a.ch1.send(received)
            }
        }
    }
}

fun func4(b: B, c: C) {
    runBlocking {
        launch {
            for (i in 1..3) {
                val received = c.ch3.receive()
                b.ch2.send(received)
            }
        }
    }
}

fun func5(d: D, e: E) {
    runBlocking {
        launch {
            for (i in 1..3) {
                val received = d.ch4.receive()
                e.ch5.send(received)
            }
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    
    val a = A(ch1)
    val b = B(ch2)
    val c = C(ch3)
    val d = D(ch4)
    val e = E(ch2)  // Intentionally causing a deadlock by reusing ch2 for e

    func1(a, b)
    func2(c, e)
    func3(a, d)
    func4(b, c)
    func5(d, e)
}