/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test770
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    // Coroutine 1
    launch {
        val data = function1(channel1)
        channel2.send(data)
    }

    // Coroutine 2
    launch {
        channel1.send(10)
    }

    // Coroutine 3
    launch {
        val data = function2(channel2)
        channel3.send(data)
    }

    // Coroutine 4
    launch {
        val data = function3(channel3)
        println("Received: $data")
    }

    // Coroutine 5
    launch {
        delay(1000)
        println("Timeout reached, no deadlock")
    }
}

suspend fun function1(channel: Channel<Int>): Int {
    return channel.receive() + 1
}

suspend fun function2(channel: Channel<Int>): Int {
    return channel.receive() * 2
}

suspend fun function3(channel: Channel<Int>): Int {
    return channel.receive() - 5
}

class RunChecker770: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}