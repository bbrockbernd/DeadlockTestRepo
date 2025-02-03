/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test703
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun testDeadlock() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch {
        val value = channel1.receive()
        channel2.send(value)
    }

    launch {
        val value = channel2.receive()
        channel3.send(value)
    }

    launch {
        val value = channel3.receive()
        channel4.send(value)
    }

    launch {
        val value = channel4.receive()
        channel1.send(value)
    }

    launch {
        channel1.send(1)
        val finalValue = channel1.receive() 
        println("Final value: $finalValue")
    }
}

testDeadlock()

class RunChecker703: RunCheckerBase() {
    override fun block() = main()
}