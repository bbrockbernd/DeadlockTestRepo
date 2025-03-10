/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.generated.test527
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()
}

fun sendDataA(manager: ChannelManager) = runBlocking {
    launch {
        manager.channel1.send("Message A1")
        manager.channel2.send("Message A2")
    }
}

fun sendDataB(manager: ChannelManager) = runBlocking {
    launch {
        manager.channel3.send("Message B1")
        manager.channel4.send("Message B2")
    }
}

fun receiveData(manager: ChannelManager) = runBlocking {
    launch {
        val msg1 = manager.channel1.receive()
        val msg2 = manager.channel2.receive()
        val msg3 = manager.channel3.receive()
        val msg4 = manager.channel4.receive()
        manager.channel5.send("$msg1 $msg2 $msg3 $msg4")
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()
    sendDataA(manager)
    sendDataB(manager)
    receiveData(manager)
    
    launch {
        println(manager.channel5.receive())
    }
}