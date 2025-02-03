/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
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
package org.example.altered.test804
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerClass {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    
    suspend fun produceData() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i + 5)
        }
    }
}

class ConsumerClass {
    val channel3 = Channel<Int>(1)
    
    suspend fun consumeData(channel: Channel<Int>) {
        for (i in 1..5) {
            val data = channel.receive()
            channel3.send(data * 2)
        }
    }
}

fun main(): Unit= runBlocking {
    val producer = ProducerClass()
    val consumer = ConsumerClass()
    
    launch {
        producer.produceData()
    }
    
    launch {
        consumer.consumeData(producer.channel1)
    }
    
    launch {
        consumer.consumeData(producer.channel2)
    }
    
    launch {
        repeat(10) {
            println("Received: ${consumer.channel3.receive()}")
        }
    }
}

class RunChecker804: RunCheckerBase() {
    override fun block() = main()
}