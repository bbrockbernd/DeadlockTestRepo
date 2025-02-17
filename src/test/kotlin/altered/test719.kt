/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.altered.test719
import org.example.altered.test719.RunChecker719.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>, start: Int) {
    GlobalScope.launch(pool) {
        for (i in start until start + 5) {
            channel.send(i)
        }
        channel.close()
    }
}

fun consumer(channel: Channel<Int>, outputChannel: Channel<Int>) {
    GlobalScope.launch(pool) {
        for (value in channel) {
            outputChannel.send(value * value)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    // Producer coroutine 1
    launch(pool) { producer(channel1, 1) }
    
    // Producer coroutine 2
    launch(pool) { producer(channel2, 6) }
    
    // Consumer coroutine 1
    launch(pool) { consumer(channel1, channel3) }
    
    // Consumer coroutine 2
    launch(pool) { consumer(channel2, channel4) }
    
    // Aggregating output
    launch(pool) {
        for (value in channel3) {
            println("Output from channel 3: $value")
        }
    }

    launch(pool) {
        for (value in channel4) {
            println("Output from channel 4: $value")
        }
    }
}

class RunChecker719: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}