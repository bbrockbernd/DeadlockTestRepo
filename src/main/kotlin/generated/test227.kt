/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.generated.test227
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageHandler {
    val channel = Channel<String>()

    suspend fun sendMessage(message: String) {
        channel.send(message)
    }

    suspend fun receiveMessage(): String {
        return channel.receive()
    }

    fun startCoroutines() = runBlocking {
        launch { coroutineA() }
        launch { coroutineB() }
        launch { coroutineC() }
        launch { coroutineD() }
        launch { coroutineE() }
        launch { coroutineF() }
        launch { coroutineG() }
    }

    suspend fun coroutineA() {
        val message = "Hello from A"
        sendMessage(message)
    }

    suspend fun coroutineB() {
        val message = receiveMessage()
        println("B received: $message")
        sendMessage("Reply from B")
    }

    suspend fun coroutineC() {
        sendMessage("Hello from C")
    }

    suspend fun coroutineD() {
        val message = receiveMessage()
        println("D received: $message")
    }

    suspend fun coroutineE() {
        sendMessage("Hello from E")
    }

    suspend fun coroutineF() {
        val message = receiveMessage()
        println("F received: $message")
    }

    suspend fun coroutineG() {
        val message = receiveMessage()
        println("G received: $message")
    }
}

fun main(): Unit{
    val handler = MessageHandler()
    handler.startCoroutines()
}