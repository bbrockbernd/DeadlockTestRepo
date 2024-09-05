/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
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
package org.example.generated.test399
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>, private val data: Int) {
    suspend fun produce() {
        for (i in 1..data) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): List<Int> {
        val resultList = mutableListOf<Int>()
        for (element in channel) {
            resultList.add(element)
        }
        return resultList
    }
}

fun initiateChannels(): Pair<Channel<Int>, Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    return Pair(channel1, channel2)
}

suspend fun String.passData(channel: Channel<Int>) {
    val number = this.toIntOrNull() ?: 0
    channel.send(number)
    channel.close()
}

suspend fun retrieveData(channel: Channel<Int>): Int {
    var sum = 0
    for (element in channel) {
        sum += element
    }
    return sum
}

suspend fun runCoroutines(channel1: Channel<Int>, channel2: Channel<Int>, producer: Producer, consumer: Consumer): Int {
    coroutineScope {
        launch { producer.produce() }
        launch { channel1.send(consumer.consume().sum()) }
        launch { channel2.send(retrieveData(channel1) + retrieveData(channel2)) }
        launch { "123".passData(channel1) }
    }
    return retrieveData(channel2)
}

fun main(): Unit = runBlocking {
    val (channel1, channel2) = initiateChannels()
    val producer = Producer(channel1, 5)
    val consumer = Consumer(channel1)
    val result = runCoroutines(channel1, channel2, producer, consumer)
    println("Result: $result")
}