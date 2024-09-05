/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.generated.test540
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MyChannelHandler(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }

    suspend fun processData() {
        val data = receiveData()
        println("Processed: $data")
    }
}

fun firstFunction(handler: MyChannelHandler) = runBlocking {
    launch {
        handler.sendData(1)
    }
}

fun secondFunction(handler: MyChannelHandler) = runBlocking {
    launch {
        handler.sendData(2)
    }
}

fun thirdFunction(handler: MyChannelHandler) = runBlocking {
    launch {
        handler.receiveData()
    }
}

fun fourthFunction(handler: MyChannelHandler) = runBlocking {
    launch {
        handler.processData()
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>(3)
    val myHandler = MyChannelHandler(channel)

    firstFunction(myHandler)
    secondFunction(myHandler)
    thirdFunction(myHandler)
    fourthFunction(myHandler)
    
    delay(1000L)  // Give some time for coroutines to complete
}