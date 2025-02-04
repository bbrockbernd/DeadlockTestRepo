/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 6 different coroutines
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
package org.example.altered.test266
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>(1)
    val channel6 = Channel<Int>(1)
    val channel7 = Channel<Int>(1)
    val channel8 = Channel<Int>(1)

    launch { producer1(channel1, channel2) }
    launch { producer2(channel3, channel4) }
    launch { producer3(channel5, channel6) }
    launch { consumer1(channel1, channel3, channel6) }
    launch { consumer2(channel2, channel4, channel5) }
    launch { consumer3(channel7, channel8) }

    launch { relay(channel7, channel8) }
}

suspend fun producer1(channel1: Channel<Int>, channel2: Channel<Int>) {
    repeat(5) {
        channel1.send(it)
        channel2.send(it * 2)
    }
    channel1.close()
    channel2.close()
}

suspend fun producer2(channel3: Channel<Int>, channel4: Channel<Int>) {
    repeat(5) {
        channel3.send(it + 10)
        channel4.send(it + 20)
    }
    channel3.close()
    channel4.close()
}

suspend fun producer3(channel5: Channel<Int>, channel6: Channel<Int>) {
    repeat(5) {
        channel5.send(it + 30)
        channel6.send(it + 40)
    }
    channel5.close()
    channel6.close()
}

suspend fun consumer1(channel1: Channel<Int>, channel3: Channel<Int>, channel6: Channel<Int>) {
    for (x in channel1) {
        println("Consumer1 received from channel1: $x")
    }
    for (x in channel3) {
        println("Consumer1 received from channel3: $x")
    }
    for (x in channel6) {
        println("Consumer1 received from channel6: $x")
    }
}

suspend fun consumer2(channel2: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) {
    for (x in channel2) {
        println("Consumer2 received from channel2: $x")
    }
    for (x in channel4) {
        println("Consumer2 received from channel4: $x")
    }
    for (x in channel5) {
        println("Consumer2 received from channel5: $x")
    }
}

suspend fun consumer3(channel7: Channel<Int>, channel8: Channel<Int>) {
    for (x in channel7) {
        println("Consumer3 received from channel7: $x")
        channel8.send(x * 2)
    }
    channel7.close()
    channel8.close()
}

suspend fun relay(channel7: Channel<Int>, channel8: Channel<Int>) {
    for (x in channel7) {
        println("Relay received from channel7: $x")
        channel8.send(x + 50)
    }
    channel7.close()
    channel8.close()
}

class RunChecker266: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}