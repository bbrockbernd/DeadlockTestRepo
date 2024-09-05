/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.generated.test903
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producerA(channelA: Channel<Int>) = GlobalScope.launch {
    repeat(5) {
        channelA.send(it)
    }
    channelA.close()
}

fun producerB(channelB: Channel<Int>, channelA: Channel<Int>) = GlobalScope.launch {
    repeat(5) {
        val value = channelA.receive() * 2
        channelB.send(value)
    }
    channelB.close()
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    producerA(channelA)
    producerB(channelB, channelA)

    repeat(5) {
        launch {
            println("Received from channelB: ${channelB.receive()}")
        }
    }
}