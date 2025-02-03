/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test640
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageHandler {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>(2)
    val channel3 = Channel<String>()

    suspend fun processMessages() {
        launch {
            val msg1 = channel1.receive()
            channel2.send(msg1)
        }

        launch {
            val msg2 = channel2.receive()
            channel3.send(msg2)
        }

        launch {
            val msg3 = channel3.receive()
            println("Processed message: $msg3")
        }
    }
}

fun main(): Unit= runBlocking {
    val handler = MessageHandler()

    launch {
        handler.channel1.send("test message")
        handler.processMessages()
    }
}

class RunChecker640: RunCheckerBase() {
    override fun block() = main()
}