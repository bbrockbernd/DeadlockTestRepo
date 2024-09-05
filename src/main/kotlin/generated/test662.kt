/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.generated.test662
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>(1)
    val ch4 = Channel<String>()

    suspend fun sendItems() {
        ch1.send(1)
        ch2.send(2)
        ch3.send(3)
        ch4.send("Hello")
    }

    suspend fun receiveAndProcess() {
        val value1 = ch1.receive()
        val value2 = ch2.receive()
        val value3 = ch3.receive()
        val message = ch4.receive()

        println("Received: $value1, $value2, $value3, $message")
    }
}

fun main(): Unit= runBlocking {
    val handler = ChannelHandler()

    launch {
        handler.sendItems()
    }

    launch {
        handler.receiveAndProcess()
    }
}