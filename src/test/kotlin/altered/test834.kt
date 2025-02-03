/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test834
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun distributeData(input: ReceiveChannel<Int>, outputs: List<SendChannel<Int>>) = runBlocking {
    for (value in input) {
        for (output in outputs) {
            output.send(value)
        }
    }
    outputs.forEach { it.close() }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    launch {
        // Sending data to channelA
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }

    launch {
        distributeData(channelA, listOf(channelB, channelC))

        for (value in channelB) {
            channelD.send(value * 2)
        }
        channelD.close()

        for (value in channelC) {
            channelE.send(value * 3)
        }
        channelE.close()
    }

    // Print values from channelD and channelE
    launch {
        for (value in channelD) {
            println("From channelD: $value")
        }
    }

    launch {
        for (value in channelE) {
            println("From channelE: $value")
        }
    }
}

class RunChecker834: RunCheckerBase() {
    override fun block() = main()
}