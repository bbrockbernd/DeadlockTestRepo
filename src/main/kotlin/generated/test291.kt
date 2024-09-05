/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test291
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.ReceiveChannel

// Class to send data through a channel
class Sender(private val sendChannel: SendChannel<Int>) {
    suspend fun sendData() {
        for (i in 1..5) {
            sendChannel.send(i)
        }
        sendChannel.close()
    }
}

class Receiver(private val receiveChannel: ReceiveChannel<Int>) {
    suspend fun receiveData(): List<Int> {
        val receivedData = mutableListOf<Int>()
        for (value in receiveChannel) {
            receivedData.add(value)
        }
        return receivedData
    }
}

fun prepareDataProcessing(sendChannel: Channel<Int>, receiveChannel: Channel<Int>, externalChannel: Channel<Int>) {
    GlobalScope.launch {
        Sender(sendChannel).sendData()
    }

    GlobalScope.launch {
        val data = Receiver(receiveChannel).receiveData()
        for (value in data) {
            externalChannel.send(value * 2)
        }
        externalChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val sendChannel = Channel<Int>()
    val receiveChannel = Channel<Int>()
    val externalChannel = Channel<Int>()

    prepareDataProcessing(sendChannel, receiveChannel, externalChannel)

    launch {
        for (value in sendChannel) {
            receiveChannel.send(value)
        }
        receiveChannel.close()
    }

    for (value in externalChannel) {
        println("Processed value: $value")
    }
}