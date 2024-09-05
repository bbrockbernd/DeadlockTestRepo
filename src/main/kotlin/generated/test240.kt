/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
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
package org.example.generated.test240
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launchProducer(channel)
    launchConsumer(channel)

    launchDeadlock(channel)
}

suspend fun launchProducer(channel: Channel<Int>) {
    coroutineScope {
        launch {
            produce(channel)
        }
    }
}

suspend fun produce(channel: Channel<Int>) {
    channel.send(1)
}

suspend fun launchConsumer(channel: Channel<Int>) {
    coroutineScope {
        launch {
            consume(channel)
        }
    }
}

suspend fun consume(channel: Channel<Int>) {
    val value = channel.receive()
    println("Received: $value")
}

suspend fun launchDeadlock(channel: Channel<Int>) {
    coroutineScope {
        launch {
            deadlock(channel)
        }
    }
}

suspend fun deadlock(channel: Channel<Int>) {
    val value = channel.receive()
    println("Deadlock value: $value")
}