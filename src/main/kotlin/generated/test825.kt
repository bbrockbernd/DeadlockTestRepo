/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.generated.test825
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelProcessor {
    val channel1 = Channel<Int>(2)
    val channel2 = Channel<Int>(2)

    suspend fun firstFunction() {
        channel1.send(1)
        val received = channel2.receive()
    }

    suspend fun secondFunction() {
        channel2.send(2)
        val received = channel1.receive()
    }

    suspend fun thirdFunction() {
        channel1.send(3)
        val received = channel2.receive()
    }

    suspend fun fourthFunction() {
        channel2.send(4)
        val received = channel1.receive()
    }
}

fun main(): Unit= runBlocking {
    val processor = ChannelProcessor()
    
    launch {
        processor.firstFunction()
    }

    launch {
        processor.secondFunction()
    }

    launch {
        processor.thirdFunction()
    }

    launch {
        processor.fourthFunction()
    }
}