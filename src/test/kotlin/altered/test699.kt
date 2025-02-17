/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test699
import org.example.altered.test699.RunChecker699.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Producing $i")
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Consuming $value")
        }
    }
}

suspend fun coroutineA(producer: Producer) {
    producer.produce()
}

suspend fun coroutineB(consumer: Consumer) {
    consumer.consume()
}

fun mainFunction() = runBlocking(pool) {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    
    launch(pool) { coroutineA(producer) }
    launch(pool) { coroutineB(consumer) }

    // This will create a deadlock since we're using an unbuffered channel
    // and both coroutines will get stuck waiting for each other.
}

fun main(): Unit{
    mainFunction()
}

class RunChecker699: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}