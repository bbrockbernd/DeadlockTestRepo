/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.generated.test796
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>) = GlobalScope.launch {
    repeat(5) {
        channel.send(it)
    }
    channel.close()
}

suspend fun consumer(channel: Channel<Int>) = coroutineScope {
    launch {
        for (value in channel) {
            println("Received $value")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    producer(channel1)
    producer(channel2)
    producer(channel3)

    consumer(channel1)
    consumer(channel2)
    consumer(channel3)
    consumer(channel4)  // This won't receive anything but will not cause deadlock
    consumer(channel5)  // This won't receive anything but will not cause deadlock

    delay(100)  // Give some time for coroutines to finish
}