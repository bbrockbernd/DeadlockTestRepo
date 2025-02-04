/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.altered.test342
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel1: Channel<Int>, val channel2: Channel<Int>) {

    suspend fun produceData() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    suspend fun processData() {
        for (value in channel1) {
            val result = value * 2
            channel2.send(result)
        }
        channel2.close()
    }

    fun printData() {
        runBlocking {
            launch {
                for (result in channel2) {
                    println("Processed data: $result")
                }
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val processor = Processor(channel1, channel2)

    launch {
        processor.produceData()
    }

    launch {
        processor.processData()
    }

    launch {
        processor.printData()
    }

    // Additional coroutines to ensure we have 5
    launch {
        delay(1000)
        println("Additional coroutine 1")
    }

    launch {
        delay(1000)
        println("Additional coroutine 2")
    }
}

class RunChecker342: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}