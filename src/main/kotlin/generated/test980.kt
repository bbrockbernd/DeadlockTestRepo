/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.generated.test980
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { producer(channel1, 1) }
    launch { intermediate(channel1, channel2, channel3) }
    launch { consumer(channel2, 2) }
    launch { intermediate(channel3, channel4, channel5) }
    launch { consumer(channel4, 3) }
}

suspend fun producer(channel: Channel<Int>, value: Int) {
    for (i in 1..5) {
        channel.send(value * i)
    }
    channel.close()
}

suspend fun intermediate(input: Channel<Int>, output1: Channel<Int>, output2: Channel<Int>) {
    for (i in input) {
        output1.send(i)
        output2.send(i * 2)
    }
    output1.close()
    output2.close()
}

suspend fun consumer(channel: Channel<Int>, multiplier: Int) {
    for (i in channel) {
        println(i * multiplier)
    }
}