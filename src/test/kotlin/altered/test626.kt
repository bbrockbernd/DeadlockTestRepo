/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.altered.test626
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun produce() {
        channel1.send(1)
        channel2.receive()
    }

    suspend fun consume() {
        channel2.send(2)
        channel1.receive()
    }
}

fun handleChannel3(channel3: Channel<String>) {
    runBlocking {
        channel3.send("Message")
    }
}

fun main(): Unit= runBlocking {
    val worker = Worker()
    val channel3 = Channel<String>()

    launch {
        worker.produce()
    }
    
    launch {
        worker.consume()
    }

    handleChannel3(channel3)

    // This will create deadlock because both coroutines are waiting for an action on the other channel.
}

class RunChecker626: RunCheckerBase() {
    override fun block() = main()
}