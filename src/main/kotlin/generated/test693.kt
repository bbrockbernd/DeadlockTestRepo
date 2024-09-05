/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.generated.test693
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { producer(channel1) }
    launch { consumer(channel1, channel2) }

    processChannel2(channel2)

    channel1.close()
    channel2.close()
}

suspend fun producer(channel1: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(i)
    }
}

suspend fun consumer(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        val received = channel1.receive()
        channel2.send(received * 2)
    }
}

suspend fun processChannel2(channel2: Channel<Int>) {
    for (i in 1..5) {
        val received = channel2.receive()
        println("Processed: $received")
    }
}

fun helperFunction1(value: Int): Int {
    return value + 1
}

fun helperFunction2(value: Int): Int {
    return value * 3
}