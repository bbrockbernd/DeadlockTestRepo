/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":3,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
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
package org.example.altered.test398
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Data(val value: Int)

class Producer(private val channel: Channel<Data>) {
    suspend fun produceData(value: Int) {
        channel.send(Data(value))
    }
}

class Consumer(private val channel: Channel<Data>) {
    suspend fun consumeData(): Data {
        return channel.receive()
    }
}

fun channelProducer(producer: Producer, startValue: Int) {
    runBlocking {
        launch {
            producer.produceData(startValue)
        }
    }
}

fun channelConsumer(consumer: Consumer): Data {
    return runBlocking {
        return@runBlocking consumer.consumeData()
    }
}

suspend fun repeatedlyProduce(producer: Producer, startValue: Int, count: Int) {
    repeat(count) {
        producer.produceData(startValue + it)
    }
}

suspend fun repeatedlyConsume(consumer: Consumer, count: Int): List<Data> {
    val dataList = mutableListOf<Data>()
    repeat(count) {
        dataList.add(consumer.consumeData())
    }
    return dataList
}

fun main(): Unit {
    val channel = Channel<Data>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    runBlocking {
        launch {
            repeatedlyProduce(producer, 0, 5)
        }
        launch {
            val dataList = repeatedlyConsume(consumer, 5)
            dataList.forEach { println(it) }
        }
    }
}

class RunChecker398: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}