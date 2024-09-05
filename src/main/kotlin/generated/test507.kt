/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.generated.test507
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produceData() {
        channel.send(1)
        channel.send(2)
        channel.send(3)
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consumeData() {
        println(channel.receive())
        println(channel.receive())
        println(channel.receive())
    }
}

fun intermediateFunction1(channel: Channel<Int>) {
    GlobalScope.launch {
        val producer = Producer(channel)
        producer.produceData()
    }
}

fun intermediateFunction2(channel: Channel<Int>) {
    GlobalScope.launch {
        val consumer = Consumer(channel)
        consumer.consumeData()
    }
}

fun connector(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..3) {
            channel2.send(channel1.receive())
        }
    }
}

fun mainFunction() = runBlocking {
    val channel1 = Channel<Int>(3)
    val channel2 = Channel<Int>(3)
    val channel3 = Channel<Int>(3)
    val channel4 = Channel<Int>(3)
    val channel5 = Channel<Int>(3)

    intermediateFunction1(channel1)
    connector(channel1, channel2)
    intermediateFunction2(channel2)
    
    coroutineScope {
        launch {
            for (i in 1..3) {
                channel4.send(channel3.receive())
            }
        }
        launch {
            for (i in 1..3) {
                channel5.send(channel4.receive())
            }
        }
    }
}