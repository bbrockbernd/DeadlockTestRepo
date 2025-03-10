/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.generated.test818
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { producer(channel) }
    launch { consumer(channel) }
    launch { multiplier(channel) }
    launch { printer(channel) }
}

suspend fun producer(channel: Channel<Int>) {
    for (i in 1..5) {
        delay(100)
        channel.send(i)
    }
}

suspend fun consumer(channel: Channel<Int>) {
    repeat(5) {
        val received = channel.receive()
        processReceived(received, channel)
    }
}

suspend fun processReceived(value: Int, channel: Channel<Int>) {
    delay(50)
    channel.send(value * 2)
}

suspend fun multiplier(channel: Channel<Int>) {
    repeat(5) {
        val received = channel.receive()
        val multiplied = multiplyValue(received)
        channel.send(multiplied)
    }
}

suspend fun multiplyValue(value: Int): Int {
    delay(30)
    return value * 3
}

suspend fun printer(channel: Channel<Int>) {
    repeat(5) {
        val received = channel.receive()
        println("Received: $received")
    }
}