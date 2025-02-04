/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 5 different coroutines
- 1 different classes

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
package org.example.altered.test86
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelDeadlockTest {
    private val channelA = Channel<Int>(1)
    private val channelB = Channel<Int>(1)

    fun initiateTest() = runBlocking {
        val job1 = launch { functionOne() }
        val job2 = launch { functionTwo() }
        val job3 = launch { functionThree() }
        val job4 = launch { functionFour() }
        val job5 = launch { functionFive() }
    }

    suspend fun functionOne() {
        val value = 1
        channelA.send(value)
        val received = channelB.receive()
        println("Function One received: $received")
    }

    suspend fun functionTwo() {
        val value = 2
        channelB.send(value)
        val received = channelA.receive()
        println("Function Two received: $received")
    }

    suspend fun functionThree() {
        val value = 3
        channelA.send(value)
        val received = channelB.receive()
        println("Function Three received: $received")
    }

    suspend fun functionFour() {
        val value = 4
        channelB.send(value)
        val received = channelA.receive()
        println("Function Four received: $received")
    }

    suspend fun functionFive() {
        val value = 5
        channelA.send(value)
        val received = channelB.receive()
        println("Function Five received: $received")
    }
}

fun main(): Unit{
    ChannelDeadlockTest().initiateTest()
}

class RunChecker86: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}