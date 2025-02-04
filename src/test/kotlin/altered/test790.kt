/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test790
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataChannel {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
}

class Processor(val dataChannel: DataChannel) {
    suspend fun processA() {
        dataChannel.channelA.send(1)
        dataChannel.channelB.receive()
    }

    suspend fun processB() {
        dataChannel.channelB.send(2)
        dataChannel.channelC.receive()
    }
}

class Coordinator(val dataChannel: DataChannel, val processor: Processor) {
    fun startCoroutines() = runBlocking {
        launch { processor.processA() }
        launch { processor.processB() }
        launch { finalProcessA() }
        launch { finalProcessB() }
    }

    suspend fun finalProcessA() {
        dataChannel.channelC.send(3)
        dataChannel.channelA.receive()
    }

    suspend fun finalProcessB() {
        dataChannel.channelA.receive()
        dataChannel.channelB.receive()
    }
}

fun main(): Unit{
    val dataChannel = DataChannel()
    val processor = Processor(dataChannel)
    val coordinator = Coordinator(dataChannel, processor)
    coordinator.startCoroutines()
}

class RunChecker790: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}