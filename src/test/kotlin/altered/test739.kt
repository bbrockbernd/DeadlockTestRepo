/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.altered.test739
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel = Channel<Int>()

fun func1() = runBlocking {
    launch {
        println("Coroutine 1: Sending 1 to channel")
        channel.send(1)
        println("Coroutine 1: Send completed")
    }
    launch { func2() }
}

suspend fun func2() {
    coroutineScope {
        launch {
            println("Coroutine 2: Receiving from channel")
            val received = channel.receive()
            println("Coroutine 2: Received $received")
        }
        launch { func3() }
    }
}

suspend fun func3() {
    coroutineScope {
        launch {
            println("Coroutine 3: Receiving from channel in func3")
            val received = channel.receive()
            println("Coroutine 3: Received $received in func3")
        }
        launch { func4() }
    }
}

suspend fun func4() {
    coroutineScope {
        launch {
            println("Coroutine 4: Receiving from channel in func4")
            val received = channel.receive()
            println("Coroutine 4: Received $received in func4")
        }
        launch { func5() }
    }
}

suspend fun func5() {
    coroutineScope {
        launch {
            println("Coroutine 5: Receiving from channel in func5")
            val received = channel.receive()
            println("Coroutine 5: Received $received in func5")
        }
    }
}

fun main(): Unit= func1()

class RunChecker739: RunCheckerBase() {
    override fun block() = main()
}