/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.altered.test616
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val job1 = launch {
        channel1.send(1)
        channel2.send(2)
    }

    val job2 = launch {
        val received1 = channel1.receive()
        channel3.send(received1)
        val received2 = channel2.receive()
        channel4.send(received2)
    }

    val job3 = launch {
        val received3 = channel3.receive()
        val received4 = channel4.receive()
        channel5.send(received3 + received4)
    }

    coroutineScope {
        val finalResult = channel5.receive()
        println("Sum: $finalResult")
    }
}

class RunChecker616: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}