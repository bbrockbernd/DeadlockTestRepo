/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 2 different channels
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
package org.example.altered.test364
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            println(channel.receive())
        }
    }
}

class Intermediate(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun relay() {
        for (i in 1..5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
    }
}

fun createProducer(channel: Channel<Int>): Producer = Producer(channel)

fun createConsumer(channel: Channel<Int>): Consumer = Consumer(channel)

fun createIntermediate(channel1: Channel<Int>, channel2: Channel<Int>): Intermediate = Intermediate(channel1, channel2)

fun runProducer(scope: CoroutineScope, producer: Producer) {
    scope.launch {
        producer.produce()
    }
}

fun runConsumer(scope: CoroutineScope, consumer: Consumer) {
    scope.launch {
        consumer.consume()
    }
}

fun runIntermediate(scope: CoroutineScope, intermediate: Intermediate) {
    scope.launch {
        intermediate.relay()
    }
}

fun mainSample() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    runBlocking {
        val producer = createProducer(channel1)
        val consumer = createConsumer(channel2)
        val intermediate = createIntermediate(channel1, channel2)

        runProducer(this, producer)
        runIntermediate(this, intermediate)
        runConsumer(this, consumer)
    }
}

mainSample()

class RunChecker364: RunCheckerBase() {
    override fun block() = main()
}