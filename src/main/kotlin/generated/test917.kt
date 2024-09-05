/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test917
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageSender(private val channel: Channel<Int>) {
    suspend fun sendMessage(message: Int) {
        channel.send(message)
    }
}

fun CoroutineScope.setupCoroutines(channel: Channel<Int>, messageSender: MessageSender) {
    launch {
        val receivedMessage = channel.receive()
        println("Coroutine 1 received: $receivedMessage")
    }
    launch {
        val receivedMessage = channel.receive()
        println("Coroutine 2 received: $receivedMessage")
    }
    launch {
        val receivedMessage = channel.receive()
        println("Coroutine 3 received: $receivedMessage")
    }
    launch {
        val receivedMessage = channel.receive()
        println("Coroutine 4 received: $receivedMessage")
    }
    launch {
        val receivedMessage = channel.receive()
        println("Coroutine 5 received: $receivedMessage")
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val messageSender = MessageSender(channel)
    
    setupCoroutines(channel, messageSender)
    
    messageSender.sendMessage(1)
    messageSender.sendMessage(2)
    messageSender.sendMessage(3)
    messageSender.sendMessage(4)
    messageSender.sendMessage(5)
    
    delay(1000L)
}