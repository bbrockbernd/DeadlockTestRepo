/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test267
import org.example.altered.test267.RunChecker267.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun processData() {
        val data = channel1.receive()
        channel2.send(data * 2)
    }
}

fun job1(channel1: Channel<Int>, channel2: Channel<Int>) {
    repeat(2) {
        GlobalScope.launch(pool) {
            channel1.send(it)
        }
    }
}

fun job2(channel1: Channel<Int>, channel2: Channel<Int>) {
    repeat(2) {
        GlobalScope.launch(pool) {
            channel2.send(it * 2)
        }
    }
}

fun job3(channel3: Channel<Int>, channel4: Channel<Int>) {
    repeat(2) {
        GlobalScope.launch(pool) {
            val data = channel3.receive()
            channel4.send(data - 1)
        }
    }
}

fun job4(channel3: Channel<Int>, channel4: Channel<Int>) {
    repeat(2) {
        GlobalScope.launch(pool) {
            val data = channel4.receive()
            channel3.send(data + 1)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val processor1 = Processor(channel1, channel2)
    val processor2 = Processor(channel3, channel4)

    // Launching eight coroutines across different jobs and processors
    launch(pool) { processor1.processData() }
    launch(pool) { processor1.processData() }
    launch(pool) { processor2.processData() }
    launch(pool) { processor2.processData() }
    launch(pool) { job1(channel1, channel2) }
    launch(pool) { job2(channel1, channel2) }
    launch(pool) { job3(channel3, channel4) }
    launch(pool) { job4(channel3, channel4) }

    delay(2000) // Give coroutines some time to lead into a deadlock

    println("End of main function execution")
}

class RunChecker267: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}