/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.altered.test839
import org.example.altered.test839.RunChecker839.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val channel: SendChannel<Int>) {
    suspend fun produce() {
        repeat(10) {
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer(private val channel: ReceiveChannel<Int>) {
    suspend fun consume(): Int {
        var sum = 0
        for (element in channel) {
            sum += element
        }
        return sum
    }
}

fun sendNumbers(channel: Channel<Int>) = runBlocking(pool) {
    Producer(channel).produce()
}

fun receiveNumbers(channel: Channel<Int>): Int = runBlocking(pool) {
    Consumer(channel).consume()
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<Int>(2)
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>(3)

    launch(pool) { sendNumbers(channel1) }
    launch(pool) { sendNumbers(channel2) }
    launch(pool) { sendNumbers(channel3) }

    println(receiveNumbers(channel1))
    println(receiveNumbers(channel2))

    launch(pool) {
        Producer(channel4).produce()
        Consumer(channel5).consume()
    }

    println(receiveNumbers(channel4))
}

class RunChecker839: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}