/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.generated.test855
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun process(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    val received = channel2.receive()
    println("Processed: $received")
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        val data = channel1.receive()
        println("Coroutine 1 received: $data")
        channel2.send(data + 1)
    }

    launch {
        val data = channel1.receive()
        println("Coroutine 2 received: $data")
        channel2.send(data + 2)
    }

    launch {
        val data = channel2.receive()
        println("Coroutine 3 received: $data")
    }

    launch {
        process(channel1, channel2)
    }

    delay(1000)
}