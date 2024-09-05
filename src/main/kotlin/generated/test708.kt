/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.generated.test708
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()
    private val channelC = Channel<Int>()

    suspend fun processA() {
        val value = channelA.receive()
        channelB.send(value + 1)
    }

    suspend fun processB() {
        val value = channelB.receive()
        channelC.send(value + 1)
    }

    suspend fun processC() {
        val value = channelC.receive()
        channelA.send(value + 1)
    }

    fun startProcessing() = runBlocking {
        launch {
            processA()
        }
        launch {
            processB()
        }
        launch {
            processC()
        }
        channelA.send(1) // Initial send to start the process
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcessing()
}