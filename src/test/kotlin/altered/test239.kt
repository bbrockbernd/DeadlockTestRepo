/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
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
package org.example.altered.test239
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<Int>)
class C(val channel: Channel<Int>)
class D(val channel: Channel<Int>)

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()
val channel7 = Channel<Int>()
val channel8 = Channel<Int>()

fun func1(a: A) {
    runBlocking {
        launch {
            a.channel.send(1)
            val result = a.channel.receive()
        }
    }
}

fun func2(b: B) {
    runBlocking {
        launch {
            b.channel.send(2)
            val result = b.channel.receive()
        }
    }
}

fun func3(c: C) {
    runBlocking {
        launch {
            c.channel.send(3)
            val result = c.channel.receive()
        }
    }
}

fun func4(d: D) {
    runBlocking {
        launch {
            d.channel.send(4)
            val result = d.channel.receive()
        }
    }
}

fun func5(a: A, b: B) {
    runBlocking {
        launch {
            a.channel.send(5)
            val result = b.channel.receive()
        }
    }
}

fun func6(c: C, d: D) {
    runBlocking {
        launch {
            c.channel.send(6)
            val result = d.channel.receive()
        }
    }
}

fun func7(a: A, c: C) {
    runBlocking {
        launch {
            c.channel.send(a.channel.receive())
        }
    }
}

fun func8(b: B, d: D) {
    runBlocking {
        launch {
            d.channel.send(b.channel.receive())
        }
    }
}

fun main(): Unit{
    val a = A(channel1)
    val b = B(channel2)
    val c = C(channel3)
    val d = D(channel4)

    func1(a)
    func2(b)
    func3(c)
    func4(d)
    func5(a, b)
    func6(c, d)
    func7(a, c)
    func8(b, d)
}

class RunChecker239: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}