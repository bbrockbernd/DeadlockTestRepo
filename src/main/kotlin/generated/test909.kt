/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.generated.test909
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SimpleClass(val channel: Channel<Int>) {
    suspend fun sendValue(value: Int) {
        channel.send(value)
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    val simpleClass = SimpleClass(channel)

    launch {
        simpleClass.sendValue(1)
        println("Sent: 1")
    }

    launch {
        delay(100)
        println("Received: ${channel.receive()}")
    }

    launch {
        val received = channel.receive()
        println("Received: $received")
    }
}