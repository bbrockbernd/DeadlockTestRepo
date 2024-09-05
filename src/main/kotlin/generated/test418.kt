/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
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
package org.example.generated.test418
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launchProducer1(channel1)
    launchProducer2(channel2)
    launchProducer3(channel3)

    launchConsumer1(channel1)
    launchConsumer2(channel2)

    delay(1000)
}

fun CoroutineScope.launchProducer1(channel: Channel<Int>) = launch {
    for (i in 1..5) {
        channel.send(i)
        delay(100)
    }
    channel.close()
}

fun CoroutineScope.launchProducer2(channel: Channel<Int>) = launch {
    for (i in 6..10) {
        channel.send(i)
        delay(150)
    }
    channel.close()
}

fun CoroutineScope.launchProducer3(channel: Channel<Int>) = launch {
    for (i in 11..15) {
        channel.send(i)
        delay(200)
    }
    channel.close()
}

fun CoroutineScope.launchConsumer1(channel: Channel<Int>) = launch {
    for (x in channel) {
        println("Consumer1 received: $x")
        delay(50)
    }
}

fun CoroutineScope.launchConsumer2(channel: Channel<Int>) = launch {
    for (x in channel) {
        println("Consumer2 received: $x")
        delay(75)
    }
}