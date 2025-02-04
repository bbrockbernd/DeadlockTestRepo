/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.altered.test610
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageProcessor {
    private val channel1 = Channel<String>(2)
    private val channel2 = Channel<String>(2)
    private val channel3 = Channel<String>(2)
    private val channel4 = Channel<String>()

    suspend fun sendMessageToChannels(message: String, targetChannel1: Boolean, targetChannel2: Boolean, targetChannel3: Boolean, targetChannel4: Boolean) {
        if (targetChannel1) channel1.send(message)
        if (targetChannel2) channel2.send(message)
        if (targetChannel3) channel3.send(message)
        if (targetChannel4) channel4.send(message)
    }

    suspend fun processIncomingMessages() {
        coroutineScope {
            launch { processChannel(channel1) }
            launch { processChannel(channel2) }
            launch { processChannel(channel3) }
            launch { processChannel(channel4) }
        }
    }

    private suspend fun processChannel(channel: Channel<String>) {
        for (msg in channel) {
            println("Processed: $msg")
        }
    }
}

fun main(): Unit= runBlocking {
    val processor = MessageProcessor()
    launch { processor.sendMessageToChannels("Message1", true, true, false, true) }
    launch { processor.sendMessageToChannels("Message2", false, true, true, false) }
    launch { processor.sendMessageToChannels("Message3", true, false, true, false) }
    processor.processIncomingMessages()
}

class RunChecker610: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}