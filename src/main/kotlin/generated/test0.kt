/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
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
package org.example.generated.test0
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>(1) // buffered channel
val channel5 = Channel<Int>(1) // buffered channel
val channel6 = Channel<Int>()

fun functionA() = runBlocking {
    channel1.send(1)
    channel3.receive()
    channel4.send(4)
}

fun functionB() = runBlocking {
    channel2.send(2)
    channel5.receive()
    channel1.receive() // This will never be received due to the cycle
}

fun functionC() = runBlocking {
    channel4.receive()
    channel6.send(6)
    channel2.receive()
}

fun functionD() = runBlocking {
    channel5.send(5)
    channel3.send(3)
    channel6.receive()
}

fun main(): Unit = runBlocking {
    launch { functionA() }
    launch { functionB() }
    launch { functionC() }
    launch { functionD() }
}