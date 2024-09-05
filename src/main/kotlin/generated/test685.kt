/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.generated.test685
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel = Channel<Int>()

suspend fun sendData() {
    channel.send(1)
}

suspend fun receiveData() {
    channel.receive()
}

fun function1() = runBlocking {
    launch {
        sendData()
    }
    launch {
        receiveData()
    }
    launch {
        sendData() // This send will cause the deadlock as the other side already has pending sends and not receiving
    }
}

fun main(): Unit{
    function1()
}