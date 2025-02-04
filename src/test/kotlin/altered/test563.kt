/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test563
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    fun produce(channel: Channel<Int>) = runBlocking {
        channel.send(1) // This will get stuck waiting as the buffer is full
    }
}

class Consumer {
    fun consume(channel: Channel<Int>) = runBlocking {
        channel.receive() // This will get stuck waiting for an element to consume
    }
}

suspend fun functionOne(channel: Channel<Int>) {
    coroutineScope {
        launch {
            Producer().produce(channel)
        }
    }
}

suspend fun functionTwo(channel: Channel<Int>) {
    coroutineScope {
        launch {
            Consumer().consume(channel)
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>() // Unbuffered channel
    runBlocking {
        launch { functionOne(channel) }
        launch { functionTwo(channel) }
        launch { functionOne(channel) }
        launch { functionTwo(channel) }
    }
}

class RunChecker563: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}