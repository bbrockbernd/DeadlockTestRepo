/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
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
package org.example.altered.test401
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5) // Buffered channel
    val channel3 = Channel<Int>()

    launchSender1(channel1)
    launchSender2(channel2)
    
    launchReceiver1(channel1)
    launchReceiver2(channel2)
    launchRelay(channel1, channel3)
    launchReceiver3(channel3)
    launchSender3(channel1)

    delay(1000) // Let coroutines finish
}

fun CoroutineScope.launchSender1(channel: Channel<Int>) = launch {
    for (i in 1..5) {
        channel.send(i)
    }
}

fun CoroutineScope.launchSender2(channel: Channel<Int>) = launch {
    for (i in 1..5) {
        channel.send(i * 10)
    }
}

fun CoroutineScope.launchReceiver1(channel: Channel<Int>) = launch {
    repeat(5) {
        println("Receiver1 received: ${channel.receive()}")
    }
}

fun CoroutineScope.launchReceiver2(channel: Channel<Int>) = launch {
    repeat(5) {
        println("Receiver2 received: ${channel.receive()}")
    }
}

fun CoroutineScope.launchRelay(channelIn: Channel<Int>, channelOut: Channel<Int>) = launch {
    repeat(5) {
        val receivedValue = channelIn.receive()
        channelOut.send(receivedValue * 2)
    }
}

fun CoroutineScope.launchReceiver3(channel: Channel<Int>) = launch {
    repeat(5) {
        println("Receiver3 received: ${channel.receive()}")
    }
}

fun CoroutineScope.launchSender3(channel: Channel<Int>) = launch {
    for (i in 6..10) {
        channel.send(i)
    }
}

class RunChecker401: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}