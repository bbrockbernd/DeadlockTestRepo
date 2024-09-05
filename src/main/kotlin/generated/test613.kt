/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 2 different coroutines
- 1 different classes

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
package org.example.generated.test613
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDetector(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun processChannel1() {
        ch1.send(1)
        ch2.receive()
    }

    suspend fun processChannel2() {
        ch2.send(2)
        ch3.receive()
    }
}

suspend fun processChannel3(ch3: Channel<Int>, ch4: Channel<Int>) {
    ch3.send(3)
    ch4.receive()
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val detector = DeadlockDetector(ch1, ch2, ch3, ch4)

    launch {
        detector.processChannel1()
    }

    launch {
        detector.processChannel2()
    }

    processChannel3(ch3, ch4)
}