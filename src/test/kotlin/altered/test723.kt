/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 5 different coroutines
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
- lists, arrays or other datastructures
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
package org.example.altered.test723
import org.example.altered.test723.RunChecker723.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun task1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        repeat(5) {
            channel1.send(it)
            delay(50)
        }
        channel1.close()
    }

    launch(pool) {
        for (item in channel1) {
            channel2.send(item * 2)
        }
        channel2.close()
    }
}

fun task2(channel3: Channel<String>, channel4: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        val input = "Kotlin"
        channel3.send(input)
        channel3.close()
    }

    launch(pool) {
        for (item in channel3) {
            channel4.send(item.reversed())
        }
        channel4.close()
    }
}

fun task3(channel4: Channel<String>, channel5: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (item in channel4) {
            channel5.send(item.length)
        }
        channel5.close()
    }

    launch(pool) {
        for (item in channel5) {
            println("Processed length: $item")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()

    launch(pool) { task1(channel1, channel2) }
    launch(pool) { task2(channel3, channel4) }
    launch(pool) { task3(channel4, channel5) }
}

class RunChecker723: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}