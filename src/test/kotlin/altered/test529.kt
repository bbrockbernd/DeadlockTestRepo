/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.altered.test529
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun task1(channel1: Channel<Int>, channel2: Channel<String>) = runBlocking {
    launch {
        channel1.send(10)
        val result = "Task 1 completed"
        channel2.send(result)
    }
}

fun task2(channel2: Channel<String>, channel3: Channel<Double>) = runBlocking {
    launch {
        val result = channel2.receive()
        val doubleValue = 20.0
        channel3.send(doubleValue)
    }
}

fun task3(channel1: Channel<Int>, channel3: Channel<Double>) = runBlocking {
    launch {
        val intValue = channel1.receive()
        val doubleValue = channel3.receive()
        println("Task 3: intValue: $intValue, doubleValue: $doubleValue")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Double>()

    launch { task1(channel1, channel2) }
    launch { task2(channel2, channel3) }
    launch { task3(channel1, channel3) }

    delay(1000) // give some time for coroutines to complete their tasks
}

class RunChecker529: RunCheckerBase() {
    override fun block() = main()
}