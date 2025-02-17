/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test878
import org.example.altered.test878.RunChecker878.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        outChannel.send(1)
    }
}

class Consumer(private val inChannel: Channel<Int>) {
    suspend fun consume() {
        inChannel.receive()
    }
}

fun coroutine1(ch1: Channel<Int>, ch2: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(ch1)
    launch(pool) { producer.produce() }
    launch(pool) { producer.produce() }
}

fun coroutine2(ch3: Channel<Int>, ch4: Channel<Int>) = runBlocking(pool) {
    val consumer1 = Consumer(ch3)
    val consumer2 = Consumer(ch4)
    launch(pool) { consumer1.consume() }
    launch(pool) { consumer2.consume() }
}

fun coroutine3(ch5: Channel<Int>, ch1: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(ch5)
    val consumer = Consumer(ch1)
    launch(pool) { producer.produce() }
    launch(pool) { consumer.consume() }
}

fun main(): Unit= runBlocking(pool) {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    
    launch(pool) { coroutine1(ch1, ch2) }
    launch(pool) { coroutine2(ch3, ch4) }
    launch(pool) { coroutine3(ch5, ch1) }

    // Deadlock arises since the channels and coroutines are creating circular dependencies which can't resolve
}

class RunChecker878: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}