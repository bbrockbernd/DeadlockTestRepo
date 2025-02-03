/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
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
package org.example.altered.test334
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class Consumer {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    suspend fun consume(channel1: Channel<Int>, channel2: Channel<Int>) {
        repeat(5) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            channel3.send(value1 + value2)
            channel4.send(value1 - value2)
        }
    }
}

fun process(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>, channel6: Channel<Int>) = runBlocking {
    val job1 = launch {
        repeat(5) {
            val value3 = channel3.receive()
            channel5.send(value3 * 2)
        }
    }
    
    val job2 = launch {
        repeat(5) {
            val value4 = channel4.receive()
            channel6.send(value4 / 2)
        }
    }
    
    launch {
        repeat(5) {
            channel3.send(channel6.receive() + 1)
        }
    }
}

val producer = Producer()
val consumer = Consumer()

runBlocking {
    val jobProducer = launch {
        producer.produce()
    }
    
    val jobConsumer = launch {
        consumer.consume(producer.channel1, producer.channel2)
    }
    
    launch {
        process(consumer.channel3, consumer.channel4, producer.channel1, producer.channel2)
    }
}

class RunChecker334: RunCheckerBase() {
    override fun block() = main()
}