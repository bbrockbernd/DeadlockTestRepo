/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test268
import org.example.altered.test268.RunChecker268.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler1(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ChannelHandler2(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ChannelHandler3(val channel5: Channel<Int>)
class DataProcessor1
class DataProcessor2

fun processData1(channelHandler1: ChannelHandler1, dataProcessor1: DataProcessor1) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                repeat(5) {
                    val received = channelHandler1.channel1.receive()
                    channelHandler1.channel2.send(received * 2)
                }
            }
        }
    }
}

fun processData2(channelHandler2: ChannelHandler2, channelHandler3: ChannelHandler3, dataProcessor2: DataProcessor2) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                repeat(5) {
                    val received = channelHandler2.channel3.receive()
                    channelHandler3.channel5.send(received + 1)
                }
            }

            launch(pool) {
                repeat(5) {
                    val received = channelHandler3.channel5.receive()
                    channelHandler2.channel4.send(received - 1)
                }
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val channelHandler1 = ChannelHandler1(channel1, channel2)
    val channelHandler2 = ChannelHandler2(channel3, channel4)
    val channelHandler3 = ChannelHandler3(channel5)
    val dataProcessor1 = DataProcessor1()
    val dataProcessor2 = DataProcessor2()

    runBlocking(pool) {
        launch(pool) {
            repeat(5) {
                channel1.send(it)
            }
        }

        launch(pool) {
            processData1(channelHandler1, dataProcessor1)
        }

        launch(pool) {
            repeat(5) {
                val received = channel2.receive()
                channel3.send(received)
            }
        }

        launch(pool) {
            processData2(channelHandler2, channelHandler3, dataProcessor2)
        }

        launch(pool) {
            repeat(5) {
                val received = channel4.receive()
                println("Processed data: $received")
            }
        }
    }
}

class RunChecker268: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}