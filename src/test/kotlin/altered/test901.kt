/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test901
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun sendToChannel1() {
        channel1.send(1)
        receiveFromChannel2()
    }

    suspend fun receiveFromChannel2() {
        val value = channel2.receive()
    }

    suspend fun sendToChannel2() {
        channel2.send(2)
        receiveFromChannel1()
    }

    suspend fun receiveFromChannel1() {
        val value = channel1.receive()
    }
}

fun main(): Unit= runBlocking {
    val handler = ChannelHandler()

    launch {
        handler.sendToChannel1()
    }
    handler.sendToChannel2()
}

class RunChecker901: RunCheckerBase() {
    override fun block() = main()
}