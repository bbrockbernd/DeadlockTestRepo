/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.generated.test423
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channelA = Channel<Int>()
val channelB = Channel<Int>()
val channelC = Channel<String>()
val channelD = Channel<String>()

fun producerOne() = runBlocking {
    launch {
        for (x in 1..5) {
            channelA.send(x)
        }
        channelA.close()
    }
}

fun producerTwo() = runBlocking {
    launch {
        for (x in 1..5) {
            channelB.send(x * x)
        }
        channelB.close()
    }
}

fun consumerOne() = runBlocking {
    launch {
        for (y in channelA) {
            channelC.send("A: $y")
        }
    }
}

fun consumerTwo() = runBlocking {
    launch {
        for (y in channelB) {
            channelD.send("B: $y")
        }
    }
}

suspend fun transformChannelA() {
    for (msg in channelC) {
        println("Received from channelC: $msg")
    }
}

suspend fun transformChannelB() {
    for (msg in channelD) {
        println("Received from channelD: $msg")
    }
}

fun runProducers() {
    producerOne()
    producerTwo()
}

fun runConsumers() = runBlocking {
    coroutineScope {
        launch { consumerOne() }
        launch { consumerTwo() }
        launch { transformChannelA() }
        launch { transformChannelB() }
    }
}

fun main(): Unit{
    runProducers()
    runConsumers()
}