/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test76
import org.example.altered.test76.RunChecker76.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelA(val channel: Channel<Int>)

class ChannelB(val channel: Channel<Int>)

class ChannelC(val channel: Channel<String>)

class ChannelD(val channel: Channel<String>)

fun produceData(channelA: ChannelA, data: Int) {
    runBlocking(pool) {
        channelA.channel.send(data)
    }
}

suspend fun consumeData(channelA: ChannelA, channelB: ChannelB) {
    val receivedData = channelA.channel.receive()
    channelB.channel.send(receivedData + 1)
}

suspend fun transformData(channelB: ChannelB, channelC: ChannelC) {
    val receivedData = channelB.channel.receive()
    channelC.channel.send("Transformed Data: $receivedData")
}

suspend fun formatData(channelC: ChannelC, channelD: ChannelD) {
    val receivedData = channelC.channel.receive()
    channelD.channel.send("Formatted Data: $receivedData")
}

fun runCoroutines(channelA: ChannelA, channelB: ChannelB, channelC: ChannelC, channelD: ChannelD) {
    runBlocking(pool) {
        launch(pool) { consumeData(channelA, channelB) }
        launch(pool) { transformData(channelB, channelC) }
        launch(pool) { formatData(channelC, channelD) }

        produceData(channelA, 42)

        coroutineScope {
            val formattedData = channelD.channel.receive()
            println(formattedData)
        }
    }
}

fun main(): Unit{
    val channelA = ChannelA(Channel<Int>())
    val channelB = ChannelB(Channel<Int>())
    val channelC = ChannelC(Channel<String>())
    val channelD = ChannelD(Channel<String>())

    runCoroutines(channelA, channelB, channelC, channelD)
}

class RunChecker76: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}