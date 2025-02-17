/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test501
import org.example.altered.test501.RunChecker501.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<String>) {
    suspend fun produceData() {
        channel1.send(42)
        channel2.send("Hello")
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<String>) {
    suspend fun consumeData(): Pair<Int, String> {
        val data1 = channel1.receive()
        val data2 = channel2.receive()
        return Pair(data1, data2)
    }
}

fun sendToThirdChannel(channel3: Channel<Pair<Int, String>>, data: Pair<Int, String>) {
    GlobalScope.launch(pool) {
        channel3.send(data)
    }
}

suspend fun processThirdChannel(channel3: Channel<Pair<Int, String>>, channel4: Channel<Int>, channel5: Channel<String>) {
    val receivedData = channel3.receive()
    channel4.send(receivedData.first)
    channel5.send(receivedData.second)
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Pair<Int, String>>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<String>()

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel1, channel2)

    launch(pool) {
        producer.produceData()
    }

    launch(pool) {
        val data = consumer.consumeData()
        sendToThirdChannel(channel3, data)
    }

    launch(pool) {
        processThirdChannel(channel3, channel4, channel5)
    }

    launch(pool) {
        val finalData1 = channel4.receive()
        println("Final Data from Channel 4: $finalData1")
    }

    launch(pool) {
        val finalData2 = channel5.receive()
        println("Final Data from Channel 5: $finalData2")
    }
}

class RunChecker501: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}