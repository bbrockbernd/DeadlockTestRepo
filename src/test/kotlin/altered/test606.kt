/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.altered.test606
import org.example.altered.test606.RunChecker606.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            inChannel.receive()
        }
    }
}

fun processor(inChannel: Channel<Int>, outChannel: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in 1..5) {
        val item = inChannel.receive()
        outChannel.send(item * 2)
    }
    outChannel.close()
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel3)

    launch(pool) {
        producer.produce()
    }
    
    launch(pool) {
        processor(channel1, channel2)
    }

    launch(pool) {
        for (i in 1..5) {
            val item = channel2.receive()
            channel3.send(item)
        }
        channel3.close()
    }

    consumer.consume()
}

class RunChecker606: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}