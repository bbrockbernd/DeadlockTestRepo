/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
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
package org.example.altered.test52
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val output: Channel<Int>) {
    suspend fun produceOne() {
        delay(100)
        output.send(1)
    }

    suspend fun produceTwo() {
        delay(100)
        output.send(2)
    }
}

class Consumer(val input: Channel<Int>) {
    suspend fun consumeOne() {
        input.receive()
        println("Consumer1 received")
    }

    suspend fun consumeTwo() {
        input.receive()
        println("Consumer2 received")
    }
}

fun producerFunction1(producer: Producer) = runBlocking {
    producer.produceOne()
}

fun producerFunction2(producer: Producer) = runBlocking {
    producer.produceTwo()
}

fun consumerFunction1(consumer: Consumer) = runBlocking {
    consumer.consumeOne()
}

fun consumerFunction2(consumer: Consumer) = runBlocking {
    consumer.consumeTwo()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)
    
    val consumer1 = Consumer(channel3)
    val consumer2 = Consumer(channel4)
    
    launch {
        producerFunction1(producer1)
    }
    
    launch {
        producerFunction2(producer2)
    }
    
    launch {
        consumerFunction1(consumer1)
    }
    
    launch {
        consumerFunction2(consumer2)
    }
    
    // This coroutine will never finish due to deadlock
    launch {
        channel3.send(channel1.receive())
        channel4.send(channel2.receive())
    }
}

class RunChecker52: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}