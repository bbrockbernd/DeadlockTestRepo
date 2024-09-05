/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.generated.test852
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            channel3.send(value1 + value2)
        }
    }
}

fun createChannels(): List<Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    return listOf(channel1, channel2, channel3, channel4, channel5)
}

fun startCoroutines(producer: Producer, consumer: Consumer) {
    val job = GlobalScope.launch {
        producer.produce()
        consumer.consume()
    }
    runBlocking { job.join() }
}

fun main(): Unit{
    val (channel1, channel2, channel3, channel4, channel5) = createChannels()
    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel3, channel2, channel4)
    
    startCoroutines(producer, consumer)
}