/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":2,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 2 different coroutines
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
package org.example.altered.test82
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun send() {
        for (i in 1..5) {
            println("Sending $i")
            sendChannel.send(i)
            receiveChannel.receive()
        }
    }
}

class Receiver(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun receive() {
        repeat(5) {
            val value = sendChannel.receive()
            println("Received $value")
            receiveChannel.send(value)
        }
    }
}

fun createChannels(): List<Channel<Int>> {
    return List(7) { Channel<Int>(1) }
}

suspend fun initiateSender(channels: List<Channel<Int>>) {
    val sender = Sender(channels[0], channels[1])
    sender.send()
}

suspend fun initiateReceiver(channels: List<Channel<Int>>) {
    val receiver = Receiver(channels[0], channels[1])
    receiver.receive()
}

fun mainFunction() = runBlocking {
    val channels = createChannels()
    val job1 = launch { initiateSender(channels) }
    val job2 = launch { initiateReceiver(channels) }
    job1.join()
    job2.join()
}

fun main(): Unit{
    mainFunction()
}

class RunChecker82: RunCheckerBase() {
    override fun block() = main()
}