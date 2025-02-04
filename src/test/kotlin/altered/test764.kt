/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test764
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun funcA() {
        val value1 = channel1.receive()
        channel2.send(value1 + 1)
    }
}

class B(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun funcB() {
        val value2 = channel3.receive()
        channel4.send(value2 + 2)
    }
}

class C(val channel5: Channel<Int>) {
    suspend fun funcC(a: A, b: B) {
        val value3 = channel5.receive()
        a.channel1.send(value3)
        b.channel3.send(value3 - 1)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel3, channel4)
    val c = C(channel5)

    launch {
        a.funcA()
    }

    launch {
        b.funcB()
    }

    c.funcC(a, b)
}

class RunChecker764: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}