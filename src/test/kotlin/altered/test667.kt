/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test667
import org.example.altered.test667.RunChecker667.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        var received = 0
        while (received < 5) {
            val item = channel.receive()
            received++
        }
    }
}

fun coroutineOne(channel: Channel<Int>) = runBlocking(pool) {
    val producer = Producer(channel)
    launch(pool) { producer.produce() }
}

fun coroutineTwo(channel: Channel<Int>) = runBlocking(pool) {
    val consumer = Consumer(channel)
    launch(pool) { consumer.consume() }
}

fun deadlockCoroutine(channel: Channel<Int>) = runBlocking(pool) {
    coroutineScope {
        coroutineOne(channel)
        coroutineTwo(channel)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    deadlockCoroutine(channel)
}

class RunChecker667: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}