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
package org.example.altered.test88
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun functionA() {
        for (i in 1..5) {
            channel1.send(i)
        }
        val result = channel2.receive()
        println("ClassA received: $result")
    }
}

class ClassB(private val channel2: Channel<Int>, private val channel3: Channel<Int>) {
    suspend fun functionB() {
        for (i in 1..5) {
            channel2.send(i * 10)
        }
        val result = channel3.receive()
        println("ClassB received: $result")
    }
}

class ClassC(private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun functionC() {
        for (i in 1..5) {
            channel3.send(i * 100)
        }
        val result = channel4.receive()
        println("ClassC received: $result")
    }
}

fun startFirstCoroutine(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    val a = ClassA(channel1, channel2)
    launch {
        a.functionA()
    }
    delay(1000)
}

fun startSecondCoroutine(channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    val b = ClassB(channel2, channel3)
    val c = ClassC(channel3, channel4)
    launch {
        b.functionB()
    }
    launch {
        c.functionC()
    }
    delay(1000)
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    startFirstCoroutine(channel1, channel2, channel3)
    startSecondCoroutine(channel2, channel3, channel4)
}

class RunChecker88: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}