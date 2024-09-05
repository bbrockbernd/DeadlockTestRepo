/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 4 different coroutines
- 5 different classes

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
package org.example.generated.test274
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<Int>)
class C(val channel: Channel<Int>)
class D(val channel: Channel<Int>)
class E(val channel: Channel<Int>)

suspend fun sendData(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun receiveData(channel: Channel<Int>): Int {
    return channel.receive()
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    
    val a = A(channel)
    val b = B(channel)
    val c = C(channel)
    val d = D(channel)
    val e = E(channel)
    
    launch {
        sendData(a.channel, 1)
        val result = receiveData(a.channel)
        println("A received: $result")
    }
    
    launch {
        sendData(b.channel, 2)
        val result = receiveData(b.channel)
        println("B received: $result")
    }
    
    launch {
        val result = receiveData(c.channel)
        println("C received: $result")
        sendData(c.channel, 3)
    }
    
    launch {
        val result = receiveData(d.channel)
        println("D received: $result")
        sendData(d.channel, 4)
    }
}