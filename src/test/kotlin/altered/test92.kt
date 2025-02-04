/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test92
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        val result = channel1.receive() // Waiting to receive from channel1
        channel2.send(result)           // Sending the received value to channel2
    }
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        val result = channel2.receive() // Waiting to receive from channel2
        channel3.send(result)           // Sending the received value to channel3
    }
}

fun function3(channel3: Channel<Int>, channel4: Channel<Int>) {
    runBlocking {
        val result = channel3.receive() // Waiting to receive from channel3
        channel4.send(result)           // Sending the received value to channel4
    }
}

fun function4(channel4: Channel<Int>, channel1: Channel<Int>) {
    runBlocking {
        val result = channel4.receive() // Waiting to receive from channel4
        channel1.send(result)           // Sending the received value to channel1
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch {
        function1(channel1, channel2)
    }
    launch {
        function2(channel2, channel3)
    }
    launch {
        function3(channel3, channel4)
    }
    launch {
        function4(channel4, channel1)
    }

    // Initial send to start the deadlock
    channel1.send(1)
}

class RunChecker92: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}