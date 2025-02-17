/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test624
import org.example.altered.test624.RunChecker624.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProducer {
    val channel1 = Channel<Int>()
    
    suspend fun produceData() {
        for (i in 1..5) {
            channel1.send(i)
            println("Sent $i")
        }
    }
}

class DataConsumer {
    val channel2 = Channel<Int>()

    suspend fun consumeData() {
        while (true) {
            val received = channel2.receive()
            println("Received $received")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val producer = DataProducer()
    val consumer = DataConsumer()

    launch(pool) {
        producer.produceData()
    }

    launch(pool) {
        consumer.consumeData()
    }

    transferData(producer.channel1, consumer.channel2)
}

suspend fun transferData(channel1: Channel<Int>, channel2: Channel<Int>) {
    while (true) {
        val data = channel1.receive()
        channel2.send(data)
    }
}

class RunChecker624: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}