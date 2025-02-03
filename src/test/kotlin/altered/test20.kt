/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":8,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test20
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataHandler {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
}

suspend fun function1(dataHandler: DataHandler) {
    for (i in 1..5) {
        dataHandler.ch1.send(i)
    }
}

suspend fun function2(dataHandler: DataHandler) {
    for (i in 1..5) {
        val received = dataHandler.ch1.receive()
        dataHandler.ch2.send(received)
    }
}

suspend fun function3(dataHandler: DataHandler) {
    for (i in 1..5) {
        val received = dataHandler.ch2.receive()
        dataHandler.ch3.send(received)
    }
}

suspend fun function4(dataHandler: DataHandler) {
    for (i in 1..5) {
        val received = dataHandler.ch3.receive()
        println("Final Output: $received")
    }
}

suspend fun function5(dataHandler: DataHandler) {
    for (i in 1..5) {
        dataHandler.ch1.receive()
    }
}

fun main(): Unit= runBlocking {
    val dataHandler = DataHandler()
    
    launch { function1(dataHandler) } // Coroutine 1
    launch { function2(dataHandler) } // Coroutine 2
    launch { function3(dataHandler) } // Coroutine 3
    launch { function4(dataHandler) } // Coroutine 4
    launch { function5(dataHandler) } // Coroutine 5
    launch { function2(dataHandler) } // Coroutine 6, duplicate intent
    launch { function3(dataHandler) } // Coroutine 7, duplicate intent
    launch { function4(dataHandler) } // Coroutine 8, duplicate intent
}

class RunChecker20: RunCheckerBase() {
    override fun block() = main()
}