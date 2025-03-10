/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.generated.test504
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()

fun sendToChannel1() = runBlocking {
    channel1.send(1)
    val received = channel3.receive()
    println("Received from channel3 in sendToChannel1: $received")
}

fun sendToChannel2() = runBlocking {
    val received = channel1.receive()
    println("Received from channel1 in sendToChannel2: $received")
    channel2.send(2)
}

fun sendToChannel3() = runBlocking {
    val received = channel2.receive()
    println("Received from channel2 in sendToChannel3: $received")
    channel3.send(3)
}

fun startCoroutines() = runBlocking {
    launch { sendToChannel1() }
    launch { sendToChannel2() }
    launch { sendToChannel3() }
}

fun main(): Unit= runBlocking {
    startCoroutines()
}