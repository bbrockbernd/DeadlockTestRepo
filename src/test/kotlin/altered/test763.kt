/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
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
package org.example.altered.test763
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    private val channel = Channel<Int>(5)

    fun startProcessing() = runBlocking {
        launch { processOne() }
        launch { processTwo() }
        launch { sendData(1) }
        launch { sendData(2) }
        launch {
            for (i in 3..5) {
                sendData(i)
            }
        }
    }

    private suspend fun processOne() {
        coroutineScope {
            repeat(2) {
                val data = channel.receive()
                println("Processing one with data: $data")
            }
        }
    }

    private suspend fun processTwo() {
        coroutineScope {
            repeat(3) {
                val data = channel.receive()
                println("Processing two with data: $data")
            }
        }
    }

    private suspend fun sendData(data: Int) {
        channel.send(data)
        println("Sent data: $data")
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcessing()
}

class RunChecker763: RunCheckerBase() {
    override fun block() = main()
}