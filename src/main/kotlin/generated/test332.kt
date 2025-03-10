/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":6,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 6 different channels
- 6 different coroutines
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
package org.example.generated.test332
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun functionA() {
        for (i in 1..5) {
            channelA.send(i)
        }
        val received = channelB.receive()
        println("ClassA received: $received")
    }
}

class ClassB(private val channelB: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun functionB() {
        val received = channelB.receive()
        println("ClassB received: $received")
        for (i in 6..10) {
            channelC.send(i)
        }
    }
}

class ClassC(private val channelC: Channel<Int>, private val channelD: Channel<Int>) {
    suspend fun functionC() {
        val received = channelC.receive()
        println("ClassC received: $received")
        for (i in 11..15) {
            channelD.send(i)
        }
    }
}

class ClassD(private val channelD: Channel<Int>, private val channelE: Channel<Int>) {
    suspend fun functionD() {
        val received = channelD.receive()
        println("ClassD received: $received")
        for (i in 16..20) {
            channelE.send(i)
        }
    }
}

class ClassE(private val channelE: Channel<Int>, private val channelF: Channel<Int>) {
    suspend fun functionE() {
        val received = channelE.receive()
        println("ClassE received: $received")
        for (i in 21..25) {
            channelF.send(i)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>(Channel.UNLIMITED)
    val channelB = Channel<Int>(Channel.UNLIMITED)
    val channelC = Channel<Int>(Channel.UNLIMITED)
    val channelD = Channel<Int>(Channel.UNLIMITED)
    val channelE = Channel<Int>(Channel.UNLIMITED)
    val channelF = Channel<Int>(Channel.UNLIMITED)

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelB, channelC)
    val classC = ClassC(channelC, channelD)
    val classD = ClassD(channelD, channelE)
    val classE = ClassE(channelE, channelF)

    launch { classA.functionA() }
    launch { classB.functionB() }
    launch { classC.functionC() }
    launch { classD.functionD() }
    launch { classE.functionE() }

    launch {
        for (i in 26..30) {
            channelF.send(i)
        }
    }
}