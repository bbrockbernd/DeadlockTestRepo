/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
- 2 different coroutines
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
package org.example.altered.test77
import org.example.altered.test77.RunChecker77.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer1(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking(pool) {
        repeat(5) {
            channelA.send(it)
            delay(100L)
            channelB.send(it * 2)
        }
        channelA.close()
        channelB.close()
    }
}

fun producer2(channelC: Channel<Int>, channelD: Channel<Int>) {
    runBlocking(pool) {
        repeat(5) {
            channelC.send(it + 5)
            delay(150L)
            channelD.send((it + 5) * 2)
        }
        channelC.close()
        channelD.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()

    launch(pool) {
        producer1(channelA, channelB)
    }
    
    launch(pool) {
        producer2(channelC, channelD)
    }

    launch(pool) {
        for (value in channelA) {
            println("ChannelA received: $value")
        }
        for (value in channelB) {
            println("ChannelB received: $value")
            channelE.send(value)
        }
        channelE.close()
    }

    launch(pool) {
        for (value in channelC) {
            println("ChannelC received: $value")
        }
        for (value in channelD) {
            println("ChannelD received: $value")
            channelF.send(value)
        }
        channelF.close()
    }

    launch(pool) {
        for (value in channelE) {
            println("ChannelE processed: $value")
        }
    }

    launch(pool) {
        for (value in channelF) {
            println("ChannelF processed: $value")
        }
    }
}

class RunChecker77: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}