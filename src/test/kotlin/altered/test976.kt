/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 3 different coroutines
- 3 different classes

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
package org.example.altered.test976
import org.example.altered.test976.RunChecker976.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val channel1: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }
}

class Producer2(val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            channel2.send(i)
        }
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun consume() {
        while (true) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            println("Consumed: $value1 and $value2")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val consumer = Consumer(channel1, channel2)
    
    launch(pool) { producer1.produce() }
    launch(pool) { producer2.produce() }
    launch(pool) { consumer.consume() }
}

class RunChecker976: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}