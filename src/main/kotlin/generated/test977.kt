/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.generated.test977
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val chA: Channel<Int>, val chB: Channel<Int>) {
    suspend fun functionA() {
        val dataA = chA.receive()
        val dataB = chB.receive()
        // Doing some work
        chA.send(dataA + dataB)
    }
}

class ClassB(val chC: Channel<Int>, val chD: Channel<Int>) {
    suspend fun functionB() {
        val dataC = chC.receive()
        val dataD = chD.receive()
        // Doing some work
        chC.send(dataC * dataD)
    }
}

class ClassC(val chE: Channel<Int>, val chA: Channel<Int>) {
    suspend fun functionC() {
        val dataA = chA.receive()
        // Doing some work
        chE.send(dataA * 2)
    }
}

fun main(): Unit= runBlocking {
    val chA = Channel<Int>()
    val chB = Channel<Int>()
    val chC = Channel<Int>()
    val chD = Channel<Int>()
    val chE = Channel<Int>()

    val objA = ClassA(chA, chB)
    val objB = ClassB(chC, chD)
    val objC = ClassC(chE, chA)

    launch {
        objA.functionA()
    }

    launch {
        objB.functionB()
    }

    launch {
        objC.functionC()
    }

    chB.send(1) // Deadlock: No coroutine can progress due to cyclic dependency

    delay(1000L) // Wait some time to see the effects of deadlock (if any)
}