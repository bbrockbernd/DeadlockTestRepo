/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.altered.test671
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SenderA(private val channel: Channel<Int>) {
    suspend fun send(value: Int) {
        channel.send(value)
    }
}

class ReceiverB(private val channel: Channel<Int>) {
    suspend fun receive(): Int {
        return channel.receive()
    }
}

class ProcessorC(
    private val inputChannel: Channel<Int>, 
    private val outputChannel: Channel<Int>
) {
    suspend fun process() {
        val value = inputChannel.receive()
        val processedValue = value * 2
        outputChannel.send(processedValue)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val senderA = SenderA(channel1)
    val receiverB = ReceiverB(channel2)
    val processorC = ProcessorC(channel1, channel2)

    launch {
        senderA.send(5)
    }

    launch {
        processorC.process()
    }

    launch {
        val value = receiverB.receive()
        println("Received: $value")
    }
}

class RunChecker671: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}