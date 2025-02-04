/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
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
package org.example.altered.test704
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun communicateDeadlock(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) = runBlocking {
    launch {
        channel1.send(1)
        val received = channel2.receive()
        channel3.send(received)
    }

    launch {
        val received = channel1.receive()
        channel4.send(received)
        channel5.send(received)
    }

    launch {
        val received3 = channel3.receive()
        val received4 = channel4.receive()
        println("Received in third coroutine: $received3 and $received4")
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    communicateDeadlock(channel1, channel2, channel3, channel4, channel5)
}

class RunChecker704: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}