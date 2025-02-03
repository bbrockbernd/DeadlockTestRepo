/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test743
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>) {
    runBlocking {
        channel.send(42)
    }
}

fun consumer(channel: Channel<Int>) {
    runBlocking {
        println("Received: ${channel.receive()}")
    }
}

fun deadlockExample() {
    val channel = Channel<Int>()

    runBlocking {
        launch { producer(channel) }
        launch { consumer(channel) }
        // Deadlock arises here because `channel.send` in producer and `channel.receive` in consumer both are runBlocking
    }
}

fun main(): Unit{
    deadlockExample()
}

class RunChecker743: RunCheckerBase() {
    override fun block() = main()
}