/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.altered.test999
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun sendToChannel(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun receiveFromChannel(channel: Channel<Int>): Int {
    return channel.receive()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    launch {
        val received = receiveFromChannel(channel1)
        sendToChannel(channel2, received)
    }
    
    launch {
        val received = receiveFromChannel(channel2)
        sendToChannel(channel1, received)
    }
    
    // This is to prevent the main function from exiting immediately
    delay(1000)
}

class RunChecker999: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}