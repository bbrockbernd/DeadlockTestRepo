/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.generated.test620
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) output.send(i)
    }
}

class Consumer(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val received = input.receive()
            output.send(received + 1)
        }
    }
}

fun producerAction(channel: Channel<Int>) {
    val producer = Producer(channel)
    runBlocking { producer.produce() }
}

fun consumerAction(input: Channel<Int>, output: Channel<Int>) {
    val consumer = Consumer(input, output)
    runBlocking { consumer.consume() }
}

fun SimpleProcess(one: Channel<Int>, two: Channel<Int>, three: Channel<Int>, four: Channel<Int>, five: Channel<Int>) {
    runBlocking {
        launch { producerAction(one) }
        launch { consumerAction(one, two) }
        launch { consumerAction(two, three) }
    }
}

fun main(): Unit{
    val channelOne = Channel<Int>(2)
    val channelTwo = Channel<Int>(2)
    val channelThree = Channel<Int>(2)
    val channelFour = Channel<Int>(2)
    val channelFive = Channel<Int>(2)

    SimpleProcess(channelOne, channelTwo, channelThree, channelFour, channelFive)

    runBlocking { 
        while (!channelThree.isEmpty) {
            println(channelThree.receive())
        } 
    }
}