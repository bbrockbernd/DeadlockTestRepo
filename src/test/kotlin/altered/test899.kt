/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
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
package org.example.altered.test899
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(private val channel: Channel<Int>) {
    suspend fun send(value: Int) {
        channel.send(value)
    }
}

class B(private val channel: Channel<Int>) {
    suspend fun receive(): Int {
        return channel.receive()
    }
}

class C(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun exchange() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

fun foo(channel: Channel<Int>) {
    runBlocking {
        launch {
            val a = A(channel)
            a.send(1)
        }
    }
}

fun bar(channel: Channel<Int>) {
    runBlocking {
        launch {
            val b = B(channel)
            println("Received: ${b.receive()}")
        }
    }
}

fun baz(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val c = C(channel1, channel2)
            c.exchange()
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    runBlocking {
        launch {
            foo(channel1)
            bar(channel2)
            baz(channel3, channel4)
        }
    }
    
    runBlocking {
        launch {
            foo(channel3)
            bar(channel4)
            baz(channel1, channel2)
        }
    }
}

class RunChecker899: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}