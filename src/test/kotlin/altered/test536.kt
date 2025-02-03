/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 4 different coroutines
- 3 different classes

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
package org.example.altered.test536
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>()

    suspend fun process() {
        coroutineScope {
            launch { receiveData() }
            launch { sendData() }
        }
    }

    suspend fun receiveData() {
        val data = channel.receive()
        processData(data)
    }

    suspend fun sendData() {
        val data = 42
        channel.send(data)
    }

    private suspend fun processData(data: Int) {
        println("Processing data: $data")
    }
}

class Worker {
    val processor = Processor()

    suspend fun doWork() {
        launch {
            processor.process()
        }
    }
}

class Manager {
    val worker = Worker()

    suspend fun manage() {
        coroutineScope {
            launch { worker.doWork() }
            launch { worker.doWork() }
            launch { worker.doWork() }
            launch { worker.doWork() }
        }
    }
}

fun main(): Unit= runBlocking {
    val manager = Manager()
    manager.manage()
}

class RunChecker536: RunCheckerBase() {
    override fun block() = main()
}