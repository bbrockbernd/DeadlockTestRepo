/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":8,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 8 different coroutines
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
package org.example.altered.test440
import org.example.altered.test440.RunChecker440.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelOperations {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    fun startProducerA() {
        CoroutineScope(pool).launch(pool) {
            for (i in 1..5) {
                channelA.send(i)
                println("Sent $i to channelA")
            }
        }
    }

    fun startProducerB() {
        CoroutineScope(pool).launch(pool) {
            for (i in 1..5) {
                channelB.send(i)
                println("Sent $i to channelB")
            }
        }
    }

    fun startConsumerC() {
        CoroutineScope(pool).launch(pool) {
            repeat(5) {
                val value = channelA.receive()
                channelC.send(value)
                println("Received $value from channelA and sent to channelC")
            }
        }
    }

    fun startConsumerD() {
        CoroutineScope(pool).launch(pool) {
            repeat(5) {
                val value = channelB.receive()
                channelD.send(value)
                println("Received $value from channelB and sent to channelD")
            }
        }
    }

    suspend fun startCombinedOperation() {
        coroutineScope {
            launch(pool) { startProducerA() }
            launch(pool) { startProducerB() }
            launch(pool) { startConsumerC() }
            launch(pool) { startConsumerD() }

            launch(pool) {
                repeat(5) {
                    val value = channelC.receive()
                    channelA.send(value)
                    println("Received $value from channelC and sent back to channelA")
                }
            }

            launch(pool) {
                repeat(5) {
                    val value = channelD.receive()
                    channelB.send(value)
                    println("Received $value from channelD and sent back to channelB")
                }
            }
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelOps = ChannelOperations()
    channelOps.startCombinedOperation()
}

class RunChecker440: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}