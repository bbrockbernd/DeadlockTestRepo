/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 6 different channels
- 8 different coroutines
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
package org.example.generated.test486
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<String>()
    val channel4 = Channel<String>(5)
    val channel5 = Channel<Boolean>()
    val channel6 = Channel<Boolean>(5)
    
    suspend fun processChannel1To2() {
        repeat(5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
        channel2.close()
    }

    suspend fun processChannel2To3() {
        for (item in channel2) {
            channel3.send("Value: $item")
        }
        channel3.close()
    }

    suspend fun processChannel3To4() {
        for (item in channel3) {
            channel4.send("$item Processed")
        }
        channel4.close()
    }

    fun launchProcessing(scope: CoroutineScope) {
        scope.launch {
            channel5.send(true)
        }
        scope.launch {
            channel6.send(false)
        }
        scope.launch {
            processChannel1To2()
        }
        scope.launch {
            processChannel2To3()
        }
        scope.launch {
            processChannel3To4()
        }
    }

    suspend fun startProcessing() {
        val scope = CoroutineScope(Dispatchers.Default)
        launchProcessing(scope)
        repeat(5) {
            channel1.send(it)
        }
        channel1.close()
    }

    suspend fun processChannels() {
        coroutineScope {
            val processor = Processor()
            processor.startProcessing()
            val result1 = channel5.receive()
            val result2 = channel6.receive()
            println("Results: $result1, $result2")
        }
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()
    processor.processChannels()
}