/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test964
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    private val channelA = Channel<String>()
    private val channelB = Channel<String>()
    private val channelC = Channel<String>()

    suspend fun produceData() {
        channelA.send("Data 1")
        channelB.send("Data 2")
    }

    suspend fun processData() {
        val dataA = channelA.receive()
        val dataB = channelB.receive()
        channelC.send("$dataA processed with $dataB")
    }

    suspend fun consumeData() {
        val result = channelC.receive()
        println(result)
    }
}

fun main(): Unit= runBlocking {
    val processor = DataProcessor()

    launch { processor.produceData() }
    launch { processor.processData() }
    launch { processor.consumeData() }
    launch { 
        delay(1000L)
        processor.produceData() 
    }
}