/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test58
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelOne {
    val channel = Channel<Int>()

    suspend fun sendValue(value: Int) {
        channel.send(value)
    }

    suspend fun receiveValue(): Int {
        return channel.receive()
    }
}

class ChannelTwo {
    val channel = Channel<Int>()

    suspend fun sendValue(value: Int) {
        channel.send(value)
    }

    suspend fun receiveValue(): Int {
        return channel.receive()
    }
}

val channelThree = Channel<Int>()

fun functionOne(c1: ChannelOne, c3: Channel<Int>) = runBlocking {
    launch {
        c1.sendValue(1)
    }
    launch {
        val value = c1.receiveValue()
        functionTwo(c3, value)
    }
}

fun functionTwo(c3: Channel<Int>, value: Int) {
    runBlocking {
        launch {
            c3.send(value)
        }
    }
    runBlocking {
        launch {
            functionThree(c3)
        }
    }
}

fun functionThree(c3: Channel<Int>) {
    runBlocking {
        launch {
            val value = c3.receive()
            functionFour(value)
        }
    }
}

fun functionFour(value: Int) {
    runBlocking {
        launch {
            functionFive(channelThree, value)
        }
    }
}

fun functionFive(c3: Channel<Int>, value: Int) {
    runBlocking {
        launch {
            c3.send(value)
        }
        launch {
            val receivedValue = c3.receive()
            println("Received: $receivedValue")
        }
    }
}

fun main(): Unit = runBlocking {
    val chanOne = ChannelOne()
    functionOne(chanOne, channelThree)
}

class RunChecker58: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}