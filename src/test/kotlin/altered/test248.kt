/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test248
import org.example.altered.test248.RunChecker248.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    suspend fun produceData() {
        coroutineScope {
            launch(pool) {
                for (i in 1..5) {
                    channel1.send(i)
                    channel3.send(i)
                    channel5.send(i)
                }
            }
            launch(pool) {
                for (i in 6..10) {
                    channel2.send(i)
                    channel4.send(i)
                    channel6.send(i)
                }
            }
        }
    }

    suspend fun consumeData() {
        coroutineScope {
            launch(pool) {
                repeat(5) {
                    val data1 = channel1.receive()
                    val data3 = channel3.receive()
                    channel8.send(data1 + data3)
                }
            }
            launch(pool) {
                repeat(5) {
                    val data2 = channel2.receive()
                    val data4 = channel4.receive()
                    channel7.send(data2 + data4)
                }
            }
            launch(pool) {
                repeat(5) {
                    val data7 = channel7.receive()
                    val data8 = channel8.receive()
                    channel5.send(data7 + data8) // Creates a circular dependency
                }
            }
        }
    }
}

fun main(): Unit = runBlocking(pool) {
    val handler = ChannelHandler()

    launch(pool) {
        handler.produceData()
    }

    launch(pool) {
        handler.consumeData()
    }
}

class RunChecker248: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}