/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.generated.test832
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
}

fun sendToChannelA(processor: Processor) {
    runBlocking {
        processor.channelA.send(1)
        processor.channelB.receive()
    }
}

fun sendToChannelB(processor: Processor) {
    runBlocking {
        processor.channelB.send(2)
        processor.channelA.receive()
    }
}

fun startProcess(processor: Processor) {
    runBlocking {
        launch {
            sendToChannelA(processor)
        }
        launch {
            sendToChannelB(processor)
        }
    }
}

fun initiatePipeline() {
    val processor = Processor()
    startProcess(processor)
}

fun main(): Unit{
    initiatePipeline()
}