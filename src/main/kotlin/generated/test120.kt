/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":7,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.generated.test120
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel = Channel<Int>()
}

fun function1(handler: ChannelHandler) = runBlocking {
    launch {
        handler.channel.send(1)
        handler.channel.send(2)
    }
}

fun function2(handler: ChannelHandler) = runBlocking {
    launch {
        handler.channel.send(3)
    }
}

fun function3(handler: ChannelHandler) = runBlocking {
    launch {
        handler.channel.receive()
        handler.channel.receive() // This call will cause the deadlock
    }
}

fun function4(handler: ChannelHandler) = runBlocking {
    launch {
        repeat(2) {
            println(handler.channel.receive())
        }
    }
}

fun function5(handler: ChannelHandler) = runBlocking {
    coroutineScope {
        launch { 
            handler.channel.send(4) 
        }
        launch { 
            handler.channel.send(5) 
        }
    }
}

fun main(): Unit= runBlocking {
    val handler = ChannelHandler()

    launch { function1(handler) }
    launch { function2(handler) }
    launch { function3(handler) }
    launch { function4(handler) }
    launch { function5(handler) }

    // Extra coroutines to meet the requirement of 7 coroutines
    launch { println("Extra coroutine 1") }
    launch { println("Extra coroutine 2") }
}