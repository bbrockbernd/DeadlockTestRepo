/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 1 different coroutines
- 2 different classes

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
package org.example.generated.test688
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }
}

class Receiver(private val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

suspend fun sendValues(sender: Sender, value: Int) {
    sender.sendData(value)
}

suspend fun receiveValues(receiver: Receiver): Int {
    return receiver.receiveData()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val sender = Sender(channel1)
    val receiver = Receiver(channel2)

    launch {
        sendValues(sender, 5)
    }

    launch {
        val receivedValue = receiveValues(receiver)
        println("Received Value: $receivedValue")
    }

    delay(1000) // To ensure both coroutines get enough time to complete
    println("No deadlock detected")
}