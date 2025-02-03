/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test636
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<String>) {
    suspend fun sendMessages() {
        channel.send("Message 1")
        channel.send("Message 2")
    }
}

class Receiver(private val channel: Channel<String>) {
    suspend fun receiveMessages() {
        println(channel.receive())
        println(channel.receive())
    }
}

class Coordinator(private val sender1: Sender, private val sender2: Sender, private val receiver1: Receiver, private val receiver2: Receiver) {
    fun coordinate() = runBlocking {
        launch { sender1.sendMessages() }
        launch { sender2.sendMessages() }
        launch { receiver1.receiveMessages() }
        launch { receiver2.receiveMessages() }
    }
}

fun createSender(channel: Channel<String>): Sender {
    return Sender(channel)
}

fun createReceiver(channel: Channel<String>): Receiver {
    return Receiver(channel)
}

fun main(): Unit{
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()

    val sender1 = createSender(channel1)
    val sender2 = createSender(channel2)
    val receiver1 = createReceiver(channel1)
    val receiver2 = createReceiver(channel2)

    val coordinator = Coordinator(sender1, sender2, receiver1, receiver2)
    coordinator.coordinate()
}

class RunChecker636: RunCheckerBase() {
    override fun block() = main()
}