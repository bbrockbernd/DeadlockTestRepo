/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test629
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)

    suspend fun processA() {
        val value = withContext(Dispatchers.Default) { 42 }
        channelA.send(value)
    }

    suspend fun processB() {
        val value = channelA.receive()
        channelB.send(value * 2)
    }
}

class Aggregator {
    val channelC = Channel<Int>(1)
    val channelD = Channel<Int>(1)

    suspend fun aggregate() {
        val value1 = channelC.receive()
        val value2 = channelD.receive()
        channelC.send(value1 + value2)
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()
    val aggregator = Aggregator()
    val channelE = Channel<Int>(1)

    launch { processor.processA() }
    launch { processor.processB() }
    launch {
        aggregator.channelC.send(10)
        aggregator.channelD.send(15)
        aggregator.aggregate()
    }
    launch {
        val result = aggregator.channelC.receive()
        channelE.send(result)
    }
    launch {
        val finalResult = channelE.receive()
        println("Final result: $finalResult")
    }
}

class RunChecker629: RunCheckerBase() {
    override fun block() = main()
}