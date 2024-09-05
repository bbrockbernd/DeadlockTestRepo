/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.generated.test857
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<String>) {
    suspend fun send(data: String) {
        channel.send(data)
    }
}

class Receiver(private val channel: Channel<String>) {
    suspend fun receive(): String {
        return channel.receive()
    }
}

class Processor {
    suspend fun process(entry: String): String {
        return "Processed: $entry"
    }
}

val channelA = Channel<String>()
val channelB = Channel<String>()
val channelC = Channel<String>()
val channelD = Channel<String>()
val channelE = Channel<String>()

suspend fun sendData(sender: Sender, data: String) {
    sender.send(data)
}

suspend fun receiveAndProcess(receiver: Receiver, processor: Processor): String {
    val receivedData = receiver.receive()
    return processor.process(receivedData)
}

suspend fun coordinateCommunication(sender: Sender, receiver: Receiver, processor: Processor) {
    val data = receiveAndProcess(receiver, processor)
    sendData(sender, data)
}

fun main(): Unit= runBlocking {
    val senders = listOf(Sender(channelA), Sender(channelB), Sender(channelC), Sender(channelD), Sender(channelE))
    val receivers = listOf(Receiver(channelA), Receiver(channelB), Receiver(channelC), Receiver(channelD), Receiver(channelE))
    val processors = listOf(Processor(), Processor(), Processor(), Processor(), Processor())

    launch {
        senders.indices.forEach { idx ->
            coordinateCommunication(senders[idx], receivers[idx], processors[idx])
        }
    }

    launch {
        senders.zip(receivers).forEach { (sender, receiver) ->
            sendData(sender, "Test Data")
            println(receiver.receive())
        }
    }
}