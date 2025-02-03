/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test255
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val channel1: Channel<Int>) {
    fun produce() = runBlocking {
        for (i in 1..3) {
            channel1.send(i)
        }
    }
}

class Producer2(val channel2: Channel<Int>) {
    fun produce() = runBlocking {
        for (i in 4..6) {
            channel2.send(i)
        }
    }
}

class Consumer1(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    fun consume() = runBlocking {
        repeat(3) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
        }
    }
}

class Consumer2(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    fun consume() = runBlocking {
        repeat(3) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
        }
    }
}

suspend fun deadlockScenario(channel1: Channel<Int>, channel2: Channel<Int>) = coroutineScope {
    launch {
        Producer1(channel1).produce()
    }
    
    launch {
        Producer2(channel2).produce()
    }

    launch {
        Consumer1(channel1, channel2).consume()
    }

    launch {
        Consumer2(channel1, channel2).consume()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    deadlockScenario(channel1, channel2)
}

class RunChecker255: RunCheckerBase() {
    override fun block() = main()
}