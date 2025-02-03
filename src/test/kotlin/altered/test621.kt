/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test621
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel = Channel<Int>()

    suspend fun sendToChannel() {
        channel.send(1)
    }

    suspend fun receiveFromChannel(): Int {
        return channel.receive()
    }

    suspend fun coroutine1() {
        sendToChannel()
        receiveFromChannel()
    }

    suspend fun coroutine2() {
        receiveFromChannel()
        sendToChannel()
    }
}

fun main(): Unit= runBlocking {
    val example = DeadlockExample()
    launch {
        example.coroutine1()
    }
    launch {
        example.coroutine2()
    }
}

class RunChecker621: RunCheckerBase() {
    override fun block() = main()
}