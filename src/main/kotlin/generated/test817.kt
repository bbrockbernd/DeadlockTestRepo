/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.generated.test817
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Define a class with channel properties
class ChannelHolder {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(5)
    val channel4 = Channel<Int>(5)
}

// Function to send data to channels
suspend fun sendData(holder: ChannelHolder) {
    holder.channel1.send(1)
    holder.channel2.send(2)
    holder.channel3.send(3)
    holder.channel4.send(4)
}

// Function to receive data from channels
suspend fun receiveData(holder: ChannelHolder) {
    holder.channel1.receive()
    holder.channel2.receive()
    holder.channel3.receive()
    holder.channel4.receive()
}

// Main Coroutine Scope
fun main(): Unit= runBlocking {
    val holder = ChannelHolder()
    
    launch {
        sendData(holder)
    }
    
    launch {
        receiveData(holder)
    }
}