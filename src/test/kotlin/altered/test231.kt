/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
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
package org.example.altered.test231
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Function 1
suspend fun producerOne(channel: SendChannel<Int>) {
    repeat(5) {
        channel.send(it)
    }
}

// Function 2
suspend fun producerTwo(channel: SendChannel<Int>) {
    repeat(5) {
        channel.send(it + 10)
    }
}

// Function 3
suspend fun consumerOne(channel: ReceiveChannel<Int>) {
    for (i in 1..5) {
        println("Consumer One received: ${channel.receive()}")
    }
}

// Function 4
suspend fun consumerTwo(channel: ReceiveChannel<Int>) {
    for (i in 1..5) {
        println("Consumer Two received: ${channel.receive()}")
    }
}

// Function 5
suspend fun intermediateOne(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
    for (i in 1..5) {
        val received = inputChannel.receive()
        outputChannel.send(received * 2)
    }
}

// Function 6
suspend fun intermediateTwo(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
    for (i in 1..5) {
        val received = inputChannel.receive()
        outputChannel.send(received + 5)
    }
}

// Function 7
fun startCoroutines(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch { producerOne(channelA) }
    launch { consumerOne(channelB) }
    launch { intermediateOne(channelA, channelB) }
}

// Function 8
fun startAdditionalCoroutines(channelC: Channel<Int>, channelD: Channel<Int>) = runBlocking {
    launch { producerTwo(channelC) }
    launch { consumerTwo(channelD) }
    launch { intermediateTwo(channelC, channelD) }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    runBlocking {
        startCoroutines(channelA, channelB)
        delay(1000) // Allow some time for the first set to proceed
        startAdditionalCoroutines(channelC, channelD)
    }
}

class RunChecker231: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}