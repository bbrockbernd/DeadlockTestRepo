/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
- 4 different classes

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
package org.example.altered.test211
import org.example.altered.test211.RunChecker211.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelBridge1(val channel: Channel<Int>)
class ChannelBridge2(val channel: Channel<Int>)
class ChannelBridge3(val channel: Channel<Int>)
class ChannelBridge4(val channel: Channel<Int>)

fun generateNumbers(channel: Channel<Int>) {
    CoroutineScope(pool).launch(pool) {
        for (x in 1..5) {
            channel.send(x)
        }
        channel.close()
    }
}

fun doubleNumbers(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    CoroutineScope(pool).launch(pool) {
        for (y in inputChannel) {
            outputChannel.send(y * 2)
        }
        outputChannel.close()
    }
}

fun squareNumbers(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    CoroutineScope(pool).launch(pool) {
        for (z in inputChannel) {
            outputChannel.send(z * z)
        }
        outputChannel.close()
    }
}

fun printResults(channel: Channel<Int>) {
    CoroutineScope(pool).launch(pool) {
        for (result in channel) {
            println(result)
        }
    }
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val bridge1 = ChannelBridge1(channel1)
    val bridge2 = ChannelBridge2(channel2)
    val bridge3 = ChannelBridge3(channel3)
    val bridge4 = ChannelBridge4(channel4)

    generateNumbers(bridge1.channel)

    doubleNumbers(bridge1.channel, bridge2.channel)
    squareNumbers(bridge2.channel, bridge3.channel)
    doubleNumbers(bridge3.channel, bridge4.channel)
    
    printResults(bridge4.channel)
}

class RunChecker211: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}