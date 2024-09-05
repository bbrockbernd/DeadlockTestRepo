/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 2 different channels
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
package org.example.generated.test95
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Message(val content: String)

class MessageHandler(val channel1: Channel<Message>, val channel2: Channel<Message>)

fun generateMessage(content: String) = Message(content)

fun generateMessages(channel1: Channel<Message>) {
    repeat(5) {
        channel1.send(generateMessage("Message $it from Channel1"))
    }
}

fun processMessages(channel1: Channel<Message>, channel2: Channel<Message>) {
    repeat(5) {
        val message = channel1.receive()
        channel2.send(generateMessage("Processed: ${message.content}"))
    }
}

fun handleMessages(handler: MessageHandler) {
    runBlocking {
        launch { generateMessages(handler.channel1) }
        launch { processMessages(handler.channel1, handler.channel2) }
        launch { receiveMessages(handler.channel2) }
    }
}

fun receiveMessages(channel: Channel<Message>) {
    repeat(5) {
        println(channel.receive().content)
    }
}

fun main(): Unit{
    val channel1 = Channel<Message>()
    val channel2 = Channel<Message>()
    val handler = MessageHandler(channel1, channel2)
    handleMessages(handler)
}