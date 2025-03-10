/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
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
package org.example.generated.test788
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { coroutine1(channel1, channel2, channel3) }
    launch { coroutine2(channel2, channel3, channel4) }
    launch { coroutine3(channel1, channel5) }
    launch { coroutine4(channel4, channel5) }
    launch { coroutine5(channel3, channel5) }

    // Start the deadlock
    channel1.send(1)
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    val value = channel1.receive()
    channel2.send(value + 1)
    channel3.send(value + 2)
}

suspend fun coroutine2(channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) {
    val value1 = channel2.receive()
    channel4.send(value1 * 2)
    val value2 = channel3.receive()
    channel4.send(value2 * 3)
}

suspend fun coroutine3(channel1: Channel<Int>, channel5: Channel<Int>) {
    val value = channel1.receive()
    channel5.send(value + 10)
}

suspend fun coroutine4(channel4: Channel<Int>, channel5: Channel<Int>) {
    val value = channel4.receive()
    channel5.receive()
}

suspend fun coroutine5(channel3: Channel<Int>, channel5: Channel<Int>) {
    val value = channel3.receive()
    channel5.send(value * 5)
}