/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":7,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test126
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val channel: Channel<Int>) {
    suspend fun sendElements() {
        for (i in 1..3) {
            channel.send(i)
        }
    }
}

class Receiver(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun receiveElements() {
        for (i in 1..3) {
            val element = channel1.receive()
            channel2.send(element + 1)
        }
    }
}

fun mainFunction(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        val sender = Sender(channel1)
        val receiver = Receiver(channel1, channel2)

        launch {
            sender.sendElements()
        }
        
        launch {
            receiver.receiveElements()
        }
        
        launch {
            for (i in 1..3) {
                val element = channel2.receive()
                channel3.send(element * 2)
            }
        }

        launch {
            for (i in 1..3) {
                println("Received: ${channel3.receive()}")
            }
        }

        launch {
            for (i in 1..3) {
                channel1.receive()
            }
        }

        launch {
            for (i in 1..3) {
                channel2.receive()
            }
        }

        launch {
            for (i in 1..3) {
                channel3.receive()
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    mainFunction(channel1, channel2, channel3)
}