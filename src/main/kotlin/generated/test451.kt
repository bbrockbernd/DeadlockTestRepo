/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.generated.test451
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val name: String) {
    val messages = Channel<String>()

    suspend fun produceMessage(message: String) {
        messages.send("$name: $message")
    }

    suspend fun sendTo(receiver: Receiver) {
        val message = messages.receive()
        receiver.receiveMessage(message)
    }
}

class Receiver(val name: String) {
    val inbox = Channel<String>()

    suspend fun receiveMessage(message: String) {
        inbox.send(message)
    }

    suspend fun read(): String {
        return inbox.receive()
    }
}

fun createMessages(producer: Producer, msgs: List<String>) = runBlocking {
    coroutineScope {
        repeat(msgs.size) {
            launch {
                producer.produceMessage(msgs[it])
            }
        }
    }
}

fun distributeMessages(producer: Producer, receiver: Receiver) = runBlocking {
    coroutineScope {
        launch {
            producer.sendTo(receiver)
        }
    }
}

fun main(): Unit= runBlocking {
    val producer1 = Producer("Producer1")
    val producer2 = Producer("Producer2")

    val receiver1 = Receiver("Receiver1")
    val receiver2 = Receiver("Receiver2")
    
    val msgList1 = listOf("Message1", "Message2", "Message3")
    val msgList2 = listOf("MessageA", "MessageB", "MessageC")

    launch { createMessages(producer1, msgList1) }
    launch { createMessages(producer2, msgList2) }
    launch { distributeMessages(producer1, receiver1) }
    launch { distributeMessages(producer1, receiver2) }
    launch { distributeMessages(producer2, receiver1) }
    launch { distributeMessages(producer2, receiver2) }

    launch {
        repeat(msgList1.size) {
            println(receiver1.read())
        }
    }
    
    launch {
        repeat(msgList2.size) {
            println(receiver2.read())
        }
    }
}