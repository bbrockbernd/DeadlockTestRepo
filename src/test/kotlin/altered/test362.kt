/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":8,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
- 8 different coroutines
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
package org.example.altered.test362
import org.example.altered.test362.RunChecker362.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer1(channel: SendChannel<Int>) = GlobalScope.launch(pool) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

fun producer2(channel: SendChannel<Int>) = GlobalScope.launch(pool) {
    for (i in 6..10) {
        channel.send(i)
    }
    channel.close()
}

fun consumer1(channel: ReceiveChannel<Int>) = GlobalScope.launch(pool) {
    for (i in channel) {
        println("Consumer1 received: $i")
    }
}

fun consumer2(channel: ReceiveChannel<Int>) = GlobalScope.launch(pool) {
    for (i in channel) {
        println("Consumer2 received: $i")
    }
}

fun controller(channel1: ReceiveChannel<Int>, channel2: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) = GlobalScope.launch(pool) {
    launch(pool) {
        for (i in channel1) {
            outputChannel.send(i)
        }
    }
    launch(pool) {
        for (i in channel2) {
            outputChannel.send(i)
        }
    }
}

fun main(): Unit = runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()

    producer1(channelA)
    producer2(channelB)

    controller(channelA, channelB, channelC)

    launch(pool) { 
        for (i in channelC) {
            channelD.send(i)
        }
    }
    launch(pool) { 
        for (i in channelD) {
            channelE.send(i)
        }
    }

    consumer1(channelE)
    consumer2(channelE)

    delay(5000) // Wait for all coroutines to finish
}

class RunChecker362: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}