/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.altered.test544
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channelA: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Sending $i to channelA")
            channelA.send(i)
            println("Sending ${i * 10} to channelC")
            channelC.send(i * 10)
        }
    }
}

class Consumer(private val channelB: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            println("Receiving from channelB: ${channelB.receive()}")
            println("Receiving from channelC: ${channelC.receive()}")
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    val producer = Producer(channelA, channelC)
    val consumer = Consumer(channelB, channelC)

    launch {
        producer.produce()
    }

    launch {
        for (i in 1..5) {
            val value = channelA.receive()
            println("Forwarding $value to channelB")
            channelB.send(value)
        }
    }
    
    launch {
        consumer.consume()
    }
    
    launch {
        println("Waiting for channelC")
        channelC.receive()
    }
    
    launch {
        println("Waiting for channelA")
        channelA.receive()
    }
}

class RunChecker544: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}