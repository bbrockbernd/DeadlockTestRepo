/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
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
package org.example.altered.test802
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch {
        println("Coroutine 1 sending...")
        channel.send(1)
        println("Coroutine 1 sent")
    }

    launch {
        println("Coroutine 2 receiving...")
        val received = channel.receive()
        println("Coroutine 2 received: $received")
    }

    launch {
        println("Coroutine 3 sending...")
        channel.send(2)
        println("Coroutine 3 sent")
    }

    // Without proper handling, this setup may lead to a deadlock
    // as multiple senders and a receiver might interfere.
}

class RunChecker802: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}