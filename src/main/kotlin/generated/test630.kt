/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.generated.test630
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender {
    val channel1 = Channel<Int>(10)
    val channel2 = Channel<Int>(10)
    suspend fun sendDataToChannel1() {
        for (i in 0..9) {
            channel1.send(i)
        }
    }

    suspend fun sendDataToChannel2() {
        for (i in 10..19) {
            channel2.send(i)
        }
    }
}

class Receiver {
    val channel3 = Channel<Int>(10)
    val channel4 = Channel<Int>(10)
    suspend fun receiveDataFromChannel1(sender: Sender) {
        for (i in 0..9) {
            val data = sender.channel1.receive()
            channel3.send(data * 2)
        }
    }
   
    suspend fun receiveDataFromChannel2(sender: Sender) {
        for (i in 10..19) {
            val data = sender.channel2.receive()
            channel4.send(data * 2)
        }
    }
}

suspend fun processChannel3(receiver: Receiver) {
    for (i in 0..9) {
        val data = receiver.channel3.receive()
        println("Processed from channel 3: $data")
    }
}

suspend fun processChannel4(receiver: Receiver) {
    for (i in 10..19) {
        val data = receiver.channel4.receive()
        println("Processed from channel 4: $data")
    }
}

suspend fun mainScope() {
    val sender = Sender()
    val receiver = Receiver()

    coroutineScope {
        launch { sender.sendDataToChannel1() }
        launch { sender.sendDataToChannel2() }
        launch { receiver.receiveDataFromChannel1(sender) }
        launch { receiver.receiveDataFromChannel2(sender) }
        launch { processChannel3(receiver) }
        launch { processChannel4(receiver) }
    }
}

fun main(): Unit= runBlocking {
    mainScope()
}