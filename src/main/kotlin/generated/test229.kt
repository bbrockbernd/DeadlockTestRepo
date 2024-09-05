/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 4 different coroutines
- 5 different classes

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
package org.example.generated.test229
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Message(val content: String)
class MessageProcessor(val channel: Channel<Message>, val id: Int)
class Result(val channel: Channel<String>, val id: Int)
class MessageSender(val channel: Channel<Message>, val id: Int)
class Finalizer(val channel: Channel<String>, val id: Int)

fun produceMessages(sender: MessageSender) {
    sender.channel.trySend(Message("Hello from ${sender.id}")).isSuccess
}

suspend fun processMessages(processor: MessageProcessor, result: Result) {
    val message = processor.channel.receive()
    result.channel.send("Processor ${processor.id} processed message: ${message.content}")
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Message>()
    val channel2 = Channel<String>()

    val sender1 = MessageSender(channel1, 1)
    val sender2 = MessageSender(channel1, 2)

    val processor1 = MessageProcessor(channel1, 1)
    val processor2 = MessageProcessor(channel1, 2)

    val result1 = Result(channel2, 1)
    val result2 = Result(channel2, 2)

    val finalizer = Finalizer(channel2, 1)

    launch { produceMessages(sender1) }
    launch { produceMessages(sender2) }
    launch { processMessages(processor1, result1) }
    launch { processMessages(processor2, result2) }

    coroutineScope {
        val finalMessage1 = finalizer.channel.receive()
        val finalMessage2 = finalizer.channel.receive()

        println(finalMessage1)
        println(finalMessage2)
    }
}