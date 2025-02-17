/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test972
import org.example.altered.test972.RunChecker972.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
            delay(500)
        }
    }
}

class Consumer(val inChannel: Channel<Int>, val outChannel: Channel<Int>) {
    suspend fun consumeAndProduce() {
        for (i in 1..5) {
            val received = inChannel.receive()
            if (received % 2 == 0) {
                outChannel.send(received * 2)
            } else {
                outChannel.send(received + 1)
            }
        }
    }
}

fun coroutineOne(chan1: Channel<Int>, chan2: Channel<Int>) = GlobalScope.launch(pool) {
    Producer(chan1).produce()
    for (i in 1..5) {
        val value = chan1.receive()
        chan2.send(value)
    }
}

fun coroutineTwo(chan2: Channel<Int>, chan3: Channel<Int>, chan4: Channel<Int>) = GlobalScope.launch(pool) {
    Consumer(chan2, chan3).consumeAndProduce()
    for (i in 1..5) {
        val value = chan3.receive()
        chan4.send(value)
    }
}

fun coroutineThree(chan4: Channel<Int>, chan5: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in 1..5) {
        val value = chan4.receive()
        chan5.send(value)
    }
}

fun coroutineFour(chan5: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in 1..5) {
        println("Final received value: ${chan5.receive()}")
    }
}

fun main(): Unit= runBlocking(pool) {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()

    coroutineOne(chan1, chan2)
    coroutineTwo(chan2, chan3, chan4)
    coroutineThree(chan4, chan5)
    coroutineFour(chan5)

    delay(3000) // Wait for coroutines to work
}

class RunChecker972: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}