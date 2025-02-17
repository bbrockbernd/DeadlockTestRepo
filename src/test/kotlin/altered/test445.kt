/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":2,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 2 different coroutines
- 4 different classes

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
package org.example.altered.test445
import org.example.altered.test445.RunChecker445.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..10) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..10) {
            println(channel.receive())
        }
    }
}

class Manager1(private val producer: Producer, private val consumer: Consumer) {
    suspend fun manage() {
        coroutineScope {
            val job1 = launch(pool) { producer.produce() }
            val job2 = launch(pool) { consumer.consume() }
        }
    }
}

class Manager2(private val producer: Producer, private val consumer: Consumer) {
    suspend fun manage() {
        coroutineScope {
            val job1 = launch(pool) { producer.produce() }
            val job2 = launch(pool) { consumer.consume() }
        }
    }
}

suspend fun createProducer(channel: Channel<Int>): Producer {
    return Producer(channel)
}

suspend fun createConsumer(channel: Channel<Int>): Consumer {
    return Consumer(channel)
}

suspend fun startManagers(manager1: Manager1, manager2: Manager2) {
    coroutineScope {
        launch(pool) { manager1.manage() }
        launch(pool) { manager2.manage() }
    }
}

fun main(): Unit{
    runBlocking(pool) {
        val channel = Channel<Int>()
        val producer1 = createProducer(channel)
        val consumer1 = createConsumer(channel)
        val producer2 = createProducer(channel)
        val consumer2 = createConsumer(channel)

        val manager1 = Manager1(producer1, consumer1)
        val manager2 = Manager2(producer2, consumer2)

        startManagers(manager1, manager2)
    }
}

class RunChecker445: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}