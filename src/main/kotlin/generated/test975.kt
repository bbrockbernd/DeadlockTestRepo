/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.generated.test975
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
        // Introducing a delay to simulate complex operation
        delay(100)
    }
}

class Receiver(private val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        val data = channel.receive()
        // Introducing a delay to simulate complex operation
        delay(100)
        return data
    }
}

fun sendAndReceiveData1(sender: Sender, receiver: Receiver) = runBlocking {
    launch {
        sender.sendData(1)
        receiver.receiveData()
    }
}

fun sendAndReceiveData2(sender: Sender, receiver: Receiver) = runBlocking {
    launch {
        receiver.receiveData()
        sender.sendData(2)
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val senderA = Sender(channelA)
    val receiverA = Receiver(channelA)

    val senderB = Sender(channelB)
    val receiverB = Receiver(channelB)

    launch { sendAndReceiveData1(senderA, receiverB) }
    launch { sendAndReceiveData2(senderB, receiverA) }
}