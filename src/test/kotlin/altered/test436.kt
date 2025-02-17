/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":8,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 8 different coroutines
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
package org.example.altered.test436
import org.example.altered.test436.RunChecker436.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channelA: Channel<Int>, val channelB: Channel<String>) {
    suspend fun produceInts() {
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }

    suspend fun produceStrings() {
        for (i in 1..5) {
            channelB.send("Message $i")
        }
        channelB.close()
    }
}

class Consumer(val channelA: Channel<Int>, val channelB: Channel<String>, val channelC: Channel<Double>) {
    suspend fun consumeInts() {
        for (value in channelA) {
            println("Received int: $value")
        }
    }

    suspend fun consumeStrings() {
        for (value in channelB) {
            println("Received string: $value")
        }
    }

    suspend fun sendToChannelC() {
        for (i in 1..5) {
            channelC.send(i + 0.5)
        }
        channelC.close()
    }
}

class Aggregator(val channelC: Channel<Double>) {
    suspend fun aggregateDoubles() {
        for (value in channelC) {
            println("Received double: $value")
        }
    }
}

fun function1(channelA: Channel<Int>, channelB: Channel<String>, channelC: Channel<Double>) {
    runBlocking(pool) {
        val producer = Producer(channelA, channelB)
        launch(pool) { producer.produceInts() }
        launch(pool) { producer.produceStrings() }
    }
}

fun function2(channelA: Channel<Int>, channelB: Channel<String>, channelC: Channel<Double>) {
    runBlocking(pool) {
        val consumer = Consumer(channelA, channelB, channelC)
        launch(pool) { consumer.consumeInts() }
        launch(pool) { consumer.consumeStrings() }
    }
}

fun function3(channelA: Channel<Int>, channelB: Channel<String>, channelC: Channel<Double>) {
    runBlocking(pool) {
        val consumer = Consumer(channelA, channelB, channelC)
        launch(pool) { consumer.sendToChannelC() }
    }
}

fun function4(channelA: Channel<Int>, channelB: Channel<String>, channelC: Channel<Double>) {
    runBlocking(pool) {
        val aggregator = Aggregator(channelC)
        launch(pool) { aggregator.aggregateDoubles() }
    }
}

fun function5(channelA: Channel<Int>, channelB: Channel<String>, channelC: Channel<Double>) {
    runBlocking(pool) {
        function1(channelA, channelB, channelC)
        function2(channelA, channelB, channelC)
        function3(channelA, channelB, channelC)
        function4(channelA, channelB, channelC)
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()

    function5(channelA, channelB, channelC)
}

class RunChecker436: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}