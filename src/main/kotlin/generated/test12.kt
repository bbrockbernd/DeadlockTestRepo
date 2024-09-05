/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.generated.test12
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class CoroutineExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    fun sendToChannel1() = runBlocking {
        launch {
            repeat(5) {
                println("Sending $it to channel1")
                channel1.send(it)
            }
        }
    }

    fun receiveFromChannel1AndSendToChannel2() = runBlocking {
        launch {
            repeat(5) {
                val received = channel1.receive()
                println("Received $received from channel1 and sending to channel2")
                channel2.send(received)
            }
        }
    }

    fun sendToChannel2() = runBlocking {
        launch {
            repeat(5) {
                println("Sending $it to channel2")
                channel2.send(it)
            }
        }
    }

    suspend fun receiveFromChannel2() = coroutineScope {
        launch {
            repeat(5) {
                val received = channel2.receive()
                println("Received $received from channel2")
            }
        }
    }
}

fun main(): Unit = runBlocking {
    val example = CoroutineExample()

    // Starting coroutines to activate the deadlock
    launch {
        example.sendToChannel1()
    }
    launch {
        example.receiveFromChannel1AndSendToChannel2()
    }
    launch {
        example.sendToChannel2()
    }
    launch {
        example.receiveFromChannel2()
    }
    launch {
        example.receiveFromChannel2()
    }
}