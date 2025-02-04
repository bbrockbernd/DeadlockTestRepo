/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test895
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionOne(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..5) {
            channelA.send(i)
            val value = channelB.receive()
            println("FunctionOne received: $value")
        }
    }
}

fun functionTwo(channelB: Channel<Int>, channelC: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..5) {
            val value = channelB.receive()
            println("FunctionTwo received: $value")
            channelC.send(value)
        }
    }
}

fun functionThree(channelC: Channel<Int>, channelD: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..5) {
            val value = channelC.receive()
            println("FunctionThree received: $value")
            channelD.send(value)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    launch { functionOne(channelA, channelB) }
    launch { functionTwo(channelB, channelC) }
    launch { functionThree(channelC, channelD) }

    // The last coroutine mimicking the downstream processing.
    launch {
        for (i in 1..5) {
            val value = channelD.receive()
            println("Main received: $value")
            // Sending back to the first channel creating a cyclic dependency.
            channelB.send(value)
        }
    }
}

class RunChecker895: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}