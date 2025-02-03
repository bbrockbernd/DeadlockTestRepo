/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.altered.test370
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(1)
        }
    }
}

fun functionB(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel2.send(channel1.receive())
        }
    }
}

fun functionC(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(channel2.receive())
        }
    }
}

fun functionD(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel2.send(2)
        }
    }
}

fun functionE(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val result = channel1.receive()
            println("Function E received $result from channel1")
        }
    }
}

fun functionF(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val result = channel2.receive()
            println("Function F received $result from channel2")
        }
    }
}

fun functionG(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(3)
        }
    }
}

fun functionH(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val result = channel2.receive()
            println("Function H received $result from channel2")
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    functionA(channel1, channel2)
    functionB(channel1, channel2)
    functionC(channel1, channel2)
    functionD(channel1, channel2)
    functionE(channel1, channel2)
    functionF(channel1, channel2)
    functionG(channel1, channel2)
    functionH(channel1, channel2)
}

class RunChecker370: RunCheckerBase() {
    override fun block() = main()
}