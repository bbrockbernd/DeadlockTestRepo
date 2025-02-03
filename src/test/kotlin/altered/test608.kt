/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test608
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    var inputChannel = Channel<Int>()
    var outputChannel = Channel<Int>()
}

class Worker(val processor: Processor) {

    suspend fun sendData() {
        for (i in 1..5) {
            processor.inputChannel.send(i)
        }
    }

    suspend fun receiveData() {
        while (true) {
            val data = processor.outputChannel.receive()
            println("Received: $data")
        }
    }
}

fun processInput(processor: Processor) {
    runBlocking {
        launch {
            while (true) {
                val data = processor.inputChannel.receive()
                println("Processing: $data")
                processor.outputChannel.send(data * 2)
            }
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    val worker = Worker(processor)

    runBlocking {
        launch {
            worker.sendData()
        }

        launch {
            worker.receiveData()
        }

        launch {
            processInput(processor)
        }

        launch {
            worker.sendData()  // This will add more data to inputChannel and create potential deadlock
        }

        launch {
            processInput(processor)  // This will try to process more data and cause a deadlock
        }
    }
}

class RunChecker608: RunCheckerBase() {
    override fun block() = main()
}