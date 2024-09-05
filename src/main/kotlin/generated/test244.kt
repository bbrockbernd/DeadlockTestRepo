/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 2 different coroutines
- 5 different classes

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
package org.example.generated.test244
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Producer2(private val outChannel: Channel<String>) {
    suspend fun produce() {
        for (i in listOf("A", "B", "C")) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Consumer1(private val intChannel: Channel<Int>, private val stringChannel: Channel<String>, private val outputChannel: Channel<String>) {
    suspend fun consumeAndForward() {
        for (i in intChannel) {
            for (s in stringChannel) {
                outputChannel.send("$i$s")
            }
        }
        outputChannel.close()
    }
}

fun createChannels(): List<Channel<*>> {
    val channel1 = Channel<Int>(3)
    val channel2 = Channel<String>(3)
    val channel3 = Channel<String>(3)
    val channel4 = Channel<String>(3)
    return listOf(channel1, channel2, channel3, channel4)
}

fun createProducers(channels: List<Channel<*>>): List<Pair<Producer1, Producer2>> {
    val producer1 = Producer1(channels[0] as Channel<Int>)
    val producer2 = Producer2(channels[1] as Channel<String>)
    return listOf(Pair(producer1, producer2))
}

fun createConsumers(channels: List<Channel<*>>): List<Consumer1> {
    val consumer1 = Consumer1(channels[0] as Channel<Int>, channels[1] as Channel<String>, channels[2] as Channel<String>)
    return listOf(consumer1)
}

class Runner(private val producers: List<Pair<Producer1, Producer2>>, private val consumers: List<Consumer1>) {
    fun runCoroutines() = runBlocking {
        launch {
            producers[0].first.produce()
        }
        launch {
            producers[0].second.produce()
        }
        launch {
            consumers[0].consumeAndForward()
        }
    }
}

fun main(): Unit{
    val channels = createChannels()
    val producers = createProducers(channels)
    val consumers = createConsumers(channels)
    val runner = Runner(producers, consumers)
    runner.runCoroutines()
}