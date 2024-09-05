/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
- 3 different coroutines
- 5 different classes

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
package org.example.generated.test360
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor1 {
    fun process1(channel1: Channel<String>, message: String) = runBlocking {
        channel1.send(message)
    }
}

class Processor2 {
    fun process2(channel1: Channel<String>, channel2: Channel<String>) = runBlocking {
        val message = channel1.receive()
        channel2.send(message)
    }
}

class Processor3 {
    fun process3(channel2: Channel<String>): String = runBlocking {
        channel2.receive()
    }
}

class Processor4 {
    suspend fun process4(channel1: Channel<String>, channel2: Channel<String>) {
        val message = channel1.receive()
        channel2.send(message)
    }
}

class Processor5 {
    suspend fun communicator(channel: Channel<String>, message: String) {
        channel.send(message)
    }
}

suspend fun coroutine1(channel1: Channel<String>, processor: Processor1) {
    processor.process1(channel1, "Message from coroutine1")
}

suspend fun coroutine2(channel1: Channel<String>, channel2: Channel<String>, processor: Processor2) {
    processor.process2(channel1, channel2)
}

suspend fun coroutine3(channel2: Channel<String>, processor: Processor5) {
    val message = "Message from coroutine3"
    processor.communicator(channel2, message)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()

    val proc1 = Processor1()
    val proc2 = Processor2()
    val proc3 = Processor3()
    val proc4 = Processor4()
    val proc5 = Processor5()

    launch {
        coroutine1(channel1, proc1)
    }

    launch {
        coroutine2(channel1, channel2, proc2)
    }

    launch {
        coroutine3(channel2, proc5)
    }

    proc3.process3(channel2)
}