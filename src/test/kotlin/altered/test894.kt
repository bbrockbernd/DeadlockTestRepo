/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test894
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { producer(channel1) }
    launch { consumer(channel1, channel2) }

    delay(2000)
    closeChannels(channel1, channel2)
}

suspend fun producer(channel: Channel<Int>) {
    repeat(10) {
        channel.send(it)
        delay(100)
    }
}

suspend fun consumer(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    while (true) {
        val value = inputChannel.receive()
        val newValue = process(value)
        outputChannel.send(newValue)
    }
}

suspend fun process(value: Int): Int {
    delay(50)
    return value * 2
}

suspend fun closeChannels(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.close()
    channel2.close()
}

class RunChecker894: RunCheckerBase() {
    override fun block() = main()
}