/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 2 different coroutines
- 2 different classes

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
package org.example.altered.test666
import org.example.altered.test666.RunChecker666.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channelA: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }
}

class Consumer(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun consume() {
        for (i in channelA) {
            channelB.send(i * i)
        }
        channelB.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    
    val producer = Producer(channelA)
    val consumer = Consumer(channelA, channelB)

    launch(pool) { producer.produce() }
    launch(pool) { consumer.consume() }

    for (result in channelB) {
        println(result)
    }
}

class RunChecker666: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}