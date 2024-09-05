/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 3 different coroutines
- 2 different classes

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
package org.example.generated.test517
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        channel.send(1)
        println("Sent 1")
        channel.send(2)
        println("Sent 2")
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>) {
        println("Received ${channel.receive()}")
        println("Received ${channel.receive()}")
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch {
        Producer().produce(channel)
    }

    launch {
        Consumer().consume(channel)
    }

    launch {
        println("Waiting for all coroutines to finish")
    }
}