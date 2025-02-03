/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test731
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Sender {
    suspend fun sendToChannelA(channel: SendChannel<Int>) {
        for (i in 1..3) {
            delay(100)
            channel.send(i)
        }
    }

    suspend fun sendToChannelB(channel: SendChannel<String>) {
        for (i in 1..3) {
            delay(150)
            channel.send("Message $i")
        }
    }
}

class Receiver {
    suspend fun receiveFromChannelC(channel: ReceiveChannel<Double>) {
        for (i in 1..3) {
            println("Received from C: ${channel.receive()}")
        }
    }

    suspend fun receiveFromChannelD(channel: ReceiveChannel<Boolean>) {
        for (i in 1..3) {
            println("Received from D: ${channel.receive()}")
        }
    }
}

suspend fun sendAndReceive(
    channelA: Channel<Int>,
    channelB: Channel<String>,
    channelC: Channel<Double>,
    channelD: Channel<Boolean>
) {
    val sender = Sender()
    val receiver = Receiver()

    coroutineScope {
        launch {
            sender.sendToChannelA(channelA)
        }
        launch {
            receiver.receiveFromChannelC(channelC)
        }
        launch {
            channelC.send(1.0)
            channelC.send(2.0)
            channelC.send(3.0)
        }
        launch {
            channelD.send(true)
            channelD.send(false)
            channelD.send(true)
        }
        launch {
            receiver.receiveFromChannelD(channelD)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()
    val channelD = Channel<Boolean>()

    launch {
        sendAndReceive(channelA, channelB, channelC, channelD)
    }

    launch {
        val sender = Sender()
        sender.sendToChannelB(channelB)
    }

    while (true) {
        select<Unit> {
            channelA.onReceive {
                println("Received from A: $it")
            }
            channelB.onReceive {
                println("Received from B: $it")
            }
        }
    }
}

class RunChecker731: RunCheckerBase() {
    override fun block() = main()
}