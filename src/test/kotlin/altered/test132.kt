/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 4 different coroutines
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
package org.example.altered.test132
import org.example.altered.test132.RunChecker132.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun producer(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()

        for (i in 6..10) {
            channel2.send(i)
        }
        channel2.close()
    }
}

fun transformer(channel1: Channel<Int>, channel3: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in channel1) {
            channel3.send(x * 2)
        }
        channel3.close()
    }
}

fun consumer1(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in channel3) {
            channel4.send(x + 1)
        }
        channel4.close()
    }
}

fun consumer2(channel2: Channel<Int>, channel5: Channel<Int>, channel6: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in channel2) {
            channel5.send(x - 1)
        }
        channel5.close()

        for (x in channel6) {
            println("Received from channel6: $x")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    producer(channel1, channel2)
    transformer(channel1, channel3)
    consumer1(channel3, channel4)
    consumer2(channel2, channel5, channel6)

    launch(pool) {
        for (x in channel4) {
            channel6.send(x * 3)
        }
        channel6.close()
    }
}

class RunChecker132: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}