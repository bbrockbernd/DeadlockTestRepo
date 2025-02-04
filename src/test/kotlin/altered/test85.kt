/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
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
package org.example.altered.test85
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer1(channel: SendChannel<Int>) = GlobalScope.launch {
    repeat(5) {
        channel.send(it)
    }
    channel.close()
}

fun producer2(channel: SendChannel<String>) = GlobalScope.launch {
    listOf("A", "B", "C", "D", "E").forEach {
        channel.send(it)
    }
    channel.close()
}

fun consumer1(channel: ReceiveChannel<Int>, nextChannel: SendChannel<Int>) = GlobalScope.launch {
    for (received in channel) {
        nextChannel.send(received * 2)
    }
    nextChannel.close()
}

fun consumer2(channel: ReceiveChannel<String>, nextChannel: SendChannel<String>) = GlobalScope.launch {
    for (received in channel) {
        nextChannel.send("$received!!")
    }
    nextChannel.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<String>()

    producer1(channel1)
    producer2(channel2)

    consumer1(channel1, channel3)
    consumer2(channel2, channel4)

    consumer1(channel3, channel5)
    consumer2(channel4, channel6)

    launch {
        for (value in channel5) {
            println("Final Int: $value")
        }
    }

    launch {
        for (value in channel6) {
            println("Final String: $value")
        }
    }

    delay(1000L) // Give coroutines time to complete
}

class RunChecker85: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}