/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test893
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProducer(val channel: Channel<Int>, val value: Int) {
    suspend fun produce() {
        channel.send(value)
    }
}

class DataConsumer(val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

fun channelOperation1(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        val value = channel1.receive()
        channel2.send(value * 2)
    }
}

fun channelOperation2(channel2: Channel<Int>, channel3: Channel<Int>) {
    GlobalScope.launch {
        val value = channel2.receive()
        channel3.send(value + 1)
    }
}

suspend fun processChannels(channel1: Channel<Int>, channel3: Channel<Int>) {
    GlobalScope.launch {
        val value = channel3.receive()
        channel1.send(value / 2)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = DataProducer(channel1, 10)
    val consumer = DataConsumer(channel3)

    launch { 
        producer.produce() 
    }
    launch { 
        println(consumer.consume()) 
    }

    channelOperation1(channel1, channel2)
    channelOperation2(channel2, channel3)
    processChannels(channel1, channel3)
    
    delay(2000) // Delay to allow coroutines to potentially deadlock
}

class RunChecker893: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}