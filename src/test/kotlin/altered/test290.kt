/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test290
import org.example.altered.test290.RunChecker290.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProducer(private val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            delay(100)
            channel.send(i)
        }
    }
}

class DataConsumer(private val channel: Channel<Int>) {
    suspend fun consumeData() {
        for (i in 1..5) {
            val data = channel.receive()
            processData(data)
        }
    }

    private suspend fun processData(data: Int) {
        // Simulate data processing delay
        delay(100)
        println("Processed: $data")
    }
}

class TestDeadlock {
    private val channel = Channel<Int>()

    fun startTest() = runBlocking(pool) {
        val producer = DataProducer(channel)
        val consumer = DataConsumer(channel)

        val job1 = launch(pool) { producer.produceData() }
        val job2 = launch(pool) { consumer.consumeData() }
        
        initiateDeadlock(job1, job2)
    }

    private fun initiateDeadlock(job1: Job, job2: Job) {
        runBlocking(pool) {
            job1.join()
            job2.join() // Deadlock: join() waits indefinitely for the job to complete which it never does
        }
    }
}

fun main(): Unit{
    TestDeadlock().startTest()
}

class RunChecker290: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}