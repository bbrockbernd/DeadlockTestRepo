/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.generated.test776
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun processA() {
        val data = channelA.receive()
        channelB.send(data * 2)
    }

    suspend fun processB() {
        val data = channelB.receive()
        channelC.send(data + 1)
    }

    val channelC = Channel<Int>()

    fun startProcessing() = runBlocking {
        launch { processA() }
        launch { processB() }
        launch {
            val data = channelC.receive()
            println("Final data: $data")
        }
        launch {
            channelA.send(42)
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcessing()
}