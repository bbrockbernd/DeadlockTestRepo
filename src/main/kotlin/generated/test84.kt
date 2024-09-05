/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
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
package org.example.generated.test84
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>(10)
    val chan4 = Channel<Int>(10)

    launchProducer(chan1, chan2)
    launchConsumer(chan2, chan3, chan4)
}

fun runBlocking(block: suspend () -> Unit) = kotlinx.coroutines.runBlocking { block() }

fun launchProducer(chan1: Channel<Int>, chan2: Channel<Int>) = runBlocking {
    launch {
        produceNumbers(chan1)
        sendNumbers(chan1, chan2)
    }
}

fun launchConsumer(chan2: Channel<Int>, chan3: Channel<Int>, chan4: Channel<Int>) = runBlocking {
    launch {
        transferNumbers(chan2, chan3)
        consumeAndPrint(chan3, chan4)
    }
}

suspend fun produceNumbers(sendChannel: Channel<Int>) {
    for (i in 1..5) {
        sendChannel.send(i)
    }
    sendChannel.close()
}

suspend fun sendNumbers(receiveChannel: Channel<Int>, sendChannel: Channel<Int>) {
    for (value in receiveChannel) {
        sendChannel.send(value)
    }
    sendChannel.close()
}

suspend fun transferNumbers(receiveChannel: Channel<Int>, sendChannel: Channel<Int>) {
    for (value in receiveChannel) {
        sendChannel.send(value)
    }
    sendChannel.close()
}

suspend fun consumeAndPrint(receiveChannel: Channel<Int>, sendChannel: Channel<Int>) {
    for (value in receiveChannel) {
        println("Received: $value")
        sendChannel.send(value)
    }
    sendChannel.close()
}