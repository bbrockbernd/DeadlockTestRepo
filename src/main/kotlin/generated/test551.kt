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
package org.example.generated.test551
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun sendValues(channel: Channel<Int>, values: List<Int>) = runBlocking {
    for (value in values) {
        channel.send(value)
    }
    channel.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<Int>(5)
   
    launch {
        sendValues(channel1, listOf(1, 2, 3, 4, 5))
    }

    launch {
        repeat(5) {
            val receivedValue = channel1.receive()
            channel2.send(receivedValue * 2)
        }
        channel2.close()
    }

    launch {
        repeat(5) {
            val result = channel2.receive()
            println("Received: $result")
        }
    }

    launch {
        sendValues(channel1, listOf(6, 7, 8, 9, 10))
    }
}