/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test201
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel1: Channel<Int>) {
    suspend fun sendData() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }
}

class Receiver(private val channel2: Channel<Int>) {
    suspend fun receiveData(): List<Int> {
        val receivedData = mutableListOf<Int>()
        for (i in 1..5) {
            val data = channel2.receive()
            receivedData.add(data)
        }
        return receivedData
    }
}

class Processor(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val data = channel1.receive()
            channel2.send(data * 2)
        }
    }
}

suspend fun createSenderCoroutine(channel1: Channel<Int>): Job = coroutineScope {
    launch {
        val sender = Sender(channel1)
        sender.sendData()
    }
}

suspend fun createReceiverCoroutine(channel2: Channel<Int>): Job = coroutineScope {
    launch {
        val receiver = Receiver(channel2)
        receiver.receiveData()
    }
}

suspend fun createProcessorCoroutine(channel1: Channel<Int>, channel2: Channel<Int>): Job = coroutineScope {
    launch {
        val processor = Processor(channel1, channel2)
        processor.process()
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    createSenderCoroutine(channel1)
    createProcessorCoroutine(channel1, channel2)
    createReceiverCoroutine(channel2)
}

class RunChecker201: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}