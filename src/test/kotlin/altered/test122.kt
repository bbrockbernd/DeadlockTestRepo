/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test122
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(2)
    val channel4 = Channel<Int>(5)

    launch { producer1(channel1) }
    launch { producer2(channel2) }
    launch { consumer1(channel1, channel3) }
    launch { consumer2(channel2, channel4) }
    launch { intermediate1(channel3, channel4) }
    launch { consumer3(channel3) }
    launch { consumer4(channel4) }
}

suspend fun producer1(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

suspend fun producer2(channel: Channel<Int>) {
    for (i in 6..10) {
        channel.send(i)
    }
    channel.close()
}

suspend fun consumer1(input: Channel<Int>, output: Channel<Int>) {
    for (i in input) {
        output.send(i + 10)
    }
    output.close()
}

suspend fun consumer2(input: Channel<Int>, output: Channel<Int>) {
    for (i in input) {
        output.send(i + 20)
    }
    output.close()
}

suspend fun intermediate1(input: Channel<Int>, output: Channel<Int>) {
    for (i in input) {
        output.send(i * 2)
    }
}

suspend fun consumer3(channel: Channel<Int>) {
    for (i in channel) {
        println("Consumer3 received: $i")
    }
}

suspend fun consumer4(channel: Channel<Int>) {
    for (i in channel) {
        println("Consumer4 received: $i")
    }
}

class RunChecker122: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}