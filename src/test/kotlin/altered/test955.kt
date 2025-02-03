/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test955
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produce() {
        channel1.send(1) // Send to channel1
        val received = channel2.receive() // Receive from channel2
    }
}

class Producer2(private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun produce() {
        channel4.send(2) // Send to channel4
        val received = channel3.receive() // Receive from channel3
    }
}

fun function1(channel1: Channel<Int>) {
    runBlocking {
        launch {
            val received = channel1.receive() // Potential receive from channel1
        }
    }
}

fun function2(channel4: Channel<Int>) {
    runBlocking {
        launch {
            channel4.send(4) // Potential send to channel4
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    runBlocking {
        launch {
            val producer1 = Producer1(channel1, channel2)
            producer1.produce()
        }

        launch {
            val producer2 = Producer2(channel3, channel4)
            producer2.produce()
        }

        function1(channel1)
        function2(channel4)
    }
}

class RunChecker955: RunCheckerBase() {
    override fun block() = main()
}