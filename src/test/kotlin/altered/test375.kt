/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":4,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 4 different coroutines
- 1 different classes

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
package org.example.altered.test375
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    suspend fun produceData(ch: Channel<Int>, start: Int, end: Int) {
        for (i in start..end) {
            ch.send(i)
        }
    }

    suspend fun consumeData(ch: Channel<Int>, result: Channel<Int>) {
        for (i in (0..5)) {
            val data = ch.receive()
            result.send(data * 2)
        }
    }

    suspend fun transferData(source: Channel<Int>, destination: Channel<Int>) {
        for (i in (0..5)) {
            val data = source.receive()
            destination.send(data)
        }
    }

    suspend fun processChannels(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
        for (i in (0..5)) {
            val dataA = channelA.receive()
            val dataB = channelB.receive()
            channelC.send(dataA + dataB)
        }
    }

    suspend fun aggregateResults(ch1: Channel<Int>, ch2: Channel<Int>, result: Channel<Int>) {
        for (i in (0..5)) {
            val data = ch1.receive() + ch2.receive()
            result.send(data)
        }
    }

    fun handleChannels() {
        runBlocking {
            val channel1 = Channel<Int>()
            val channel2 = Channel<Int>()
            val channel3 = Channel<Int>()
            val channel4 = Channel<Int>()
            val result1 = Channel<Int>()
            val result2 = Channel<Int>()

            launch { produceData(channel1, 0, 5) }
            launch { consumeData(channel1, result1) }
            launch { transferData(channel2, channel3) }
            launch { processChannels(channel1, channel2, channel4) } 

            launch { aggregateResults(channel3, channel4, result2) }
        }
    }
}

fun main(): Unit{
    val handler = ChannelHandler()
    handler.handleChannels()
}

class RunChecker375: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}