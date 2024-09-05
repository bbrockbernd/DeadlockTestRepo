/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.generated.test101
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val channel: Channel<Int>) {
    suspend fun sendValues() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Receiver(val channel: Channel<Int>) {
    suspend fun receiveValues() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

class DeadlockSimulator {
    fun createDeadlock(channel: Channel<Int>) {
        runBlocking {
            // Coroutine 1
            launch {
                val sender = Sender(channel)
                sender.sendValues()
            }

            // Coroutine 2
            launch {
                val receiver = Receiver(channel)
                delay(1000)
                receiver.receiveValues()
            }

            // Coroutine 3
            launch {
                val sender = Sender(channel)
                sender.sendValues()
            }

            // Coroutine 4
            launch {
                val receiver = Receiver(channel)
                delay(500)
                receiver.receiveValues()
            }

            // Coroutine 5
            launch {
                val sender = Sender(channel)
                sender.sendValues()
            }

            // Coroutine 6
            launch {
                val receiver = Receiver(channel)
                delay(200)
                receiver.receiveValues()
            }

            // Coroutine 7
            launch {
                val sender = Sender(channel)
                sender.sendValues()
            }
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>(1) // single-buffered channel to induce deadlock
    val simulator = DeadlockSimulator()
    simulator.createDeadlock(channel)
}