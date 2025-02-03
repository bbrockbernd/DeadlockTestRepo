/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.altered.test963
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer1(channel: SendChannel<Int>) {
    GlobalScope.launch {
        for (i in 1..4) {
            channel.send(i)
        }
        channel.close()
    }
}

fun producer2(channel: SendChannel<String>) {
    GlobalScope.launch {
        val messages = listOf("A", "B", "C", "D")
        for (msg in messages) {
            channel.send(msg)
        }
        channel.close()
    }
}

fun consumer1(channel: ReceiveChannel<Int>, nextChannel: SendChannel<Int>) {
    GlobalScope.launch {
        for (value in channel) {
            nextChannel.send(value * 2)
        }
        nextChannel.close()
    }
}

fun consumer2(channel: ReceiveChannel<String>, nextChannel: SendChannel<String>) {
    GlobalScope.launch {
        for (msg in channel) {
            nextChannel.send(msg + msg)
        }
        nextChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()

    producer1(channel1)
    producer2(channel2)
    consumer1(channel1, channel3)
    consumer2(channel2, channel4)

    GlobalScope.launch {
        for (item in channel3) {
            println("Channel 3 received: $item")
        }
    }

    GlobalScope.launch {
        for (msg in channel4) {
            println("Channel 4 received: $msg")
        }
    }

    delay(1000)
}

class RunChecker963: RunCheckerBase() {
    override fun block() = main()
}