/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.generated.test642
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun sendToB(a: B) = runBlocking {
        launch {
            channel1.send(1)
            a.receiveAndSendTo(channel2)
        }
    }

    suspend fun checkDeadlock() {
        println("A: Check Deadlock")
    }
}

class B {
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    suspend fun receiveAndSendTo(channel: Channel<Int>) {
        channel3.receive()
        channel.send(2)
    }

    fun communicateWithA(a: A) = runBlocking {
        launch {
            channel4.receive()
            a.checkDeadlock()
        }
    }
}

fun main(): Unit{
    val a = A()
    val b = B()

    a.sendToB(b)
    b.communicateWithA(a)
}