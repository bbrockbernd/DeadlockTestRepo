/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.generated.test774
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { producer(channel) }
    launch { consumer1(channel) }
    launch { consumer2(channel) }

    delay(1000L)
}

suspend fun producer(channel: Channel<Int>) = coroutineScope {
    for (i in 1..5) {
        channel.send(i)
        delay(200L)
    }
    channel.close()
}

suspend fun consumer1(channel: Channel<Int>) = coroutineScope {
    for (i in channel) {
        println("Consumer 1 received: $i")
        additionalProcessing(i)
    }
}

suspend fun consumer2(channel: Channel<Int>) = coroutineScope {
    for (i in channel) {
        println("Consumer 2 received: $i")
        additionalProcessing(i)
    }
}

suspend fun additionalProcessing(value: Int) {
    delay(100L)
    println("Processed value: $value")
}