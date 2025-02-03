/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
- 7 different coroutines
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
package org.example.altered.test347
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..10) {
            channel.send(i)
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 11..20) {
            channel.send(i)
        }
    }
}

class Consumer1(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..10) {
            val received = channel.receive()
            processData(received)
        }
    }

    private fun processData(data: Int) {
        println("Consumer1 processed $data")
    }
}

class Consumer2(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..10) {
            val received = channel.receive()
            processData(received)
        }
    }

    private fun processData(data: Int) {
        println("Consumer2 processed $data")
    }
}

class ChannelManager {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun createProducer1(): Producer1 {
        return Producer1(channel1)
    }

    fun createProducer2(): Producer2 {
        return Producer2(channel2)
    }

    fun createConsumer1(): Consumer1 {
        return Consumer1(channel1)
    }

    fun createConsumer2(): Consumer2 {
        return Consumer2(channel2)
    }
}

fun main(): Unit= runBlocking {
    val channelManager = ChannelManager()

    val producer1 = channelManager.createProducer1()
    val producer2 = channelManager.createProducer2()
    val consumer1 = channelManager.createConsumer1()
    val consumer2 = channelManager.createConsumer2()

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { consumer1.consume() }
    launch { consumer2.consume() }

    extraCoroutine()
}

suspend fun extraCoroutine() = coroutineScope {
    val channelManager = ChannelManager()

    val producer3 = channelManager.createProducer1()
    val consumer3 = channelManager.createConsumer1()

    launch { producer3.produce() }
    launch { consumer3.consume() }
}

class RunChecker347: RunCheckerBase() {
    override fun block() = main()
}