/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.generated.test897
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun sendData(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

suspend fun receiveData(channel: Channel<Int>): Int {
    var sum = 0
    for (i in 1..5) {
        sum += channel.receive()
    }
    return sum
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch {
        sendData(channel)
    }
    println("Sum: ${receiveData(channel)}")
}