/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.altered.test404
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelProducer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class ChannelConsumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println(channel.receive())
        }
    }
}

fun createProducerFunction(channel: Channel<Int>): suspend () -> Unit = {
    val producer = ChannelProducer(channel)
    producer.produce()
}

fun createConsumerFunction(channel: Channel<Int>): suspend () -> Unit = {
    val consumer = ChannelConsumer(channel)
    consumer.consume()
}

fun mainFunction(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        coroutineScope {
            launch { createProducerFunction(channel1).invoke() }
            launch { createConsumerFunction(channel2).invoke() }
        }
    }
}

fun testDeadlock() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    mainFunction(channel1, channel2)
}

testDeadlock()

class RunChecker404: RunCheckerBase() {
    override fun block() = main()
}