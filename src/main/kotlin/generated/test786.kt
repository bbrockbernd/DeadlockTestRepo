/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
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
package org.example.generated.test786
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun processChannels(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(i)
        val received1 = channel2.receive()
        channel3.send(received1 * 2)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch {
        for (i in 1..5) {
            val received = channel1.receive()
            channel2.send(received + 1)
        }
    }

    launch {
        processChannels(channel1, channel2, channel3)
    }

    launch {
        for (i in 1..5) {
            println(channel3.receive())
        }
    }
}