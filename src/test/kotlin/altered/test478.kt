/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.altered.test478
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ComponentA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun functionA1() {
        val data = channel1.receive()
        channel2.send(data)
    }

    suspend fun functionA2() {
        val data = channel2.receive()
        channel1.send(data)
    }
}

class ComponentB {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    suspend fun functionB1() {
        val data = channel3.receive()
        channel4.send(data)
    }

    suspend fun functionB2() {
        val data = channel4.receive()
        channel5.send(data)
    }

    suspend fun functionB3() {
        val data = channel5.receive()
        channel6.send(data)
    }

    suspend fun functionB4() {
        val data = channel6.receive()
        channel3.send(data)
    }
}

fun main(): Unit = runBlocking {
    val componentA = ComponentA()
    val componentB = ComponentB()

    launch {
        componentA.functionA1()
    }
    launch {
        componentA.functionA2()
    }
    launch {
        componentB.functionB1()
    }
    launch {
        componentB.functionB2()
        componentB.functionB3()
        componentB.functionB4()
    }

    componentA.channel1.send(1)
    componentB.channel3.send(2)
}

class RunChecker478: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}