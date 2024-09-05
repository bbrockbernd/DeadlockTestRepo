/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 2 different coroutines
- 4 different classes

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
package org.example.generated.test259
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(val channel: Channel<Int>)
class ProducerB(val channel: Channel<Int>)
class ConsumerA(val channel: Channel<Int>)
class ConsumerB(val channel: Channel<Int>)

fun produceA(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in 1..5) channel.send(x)
        channel.close()
    }
}

fun produceB(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in 6..10) channel.send(x)
        channel.close()
    }
}

fun consumeA(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in channel) println("Consumer A received: $x")
    }
}

fun consumeB(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in channel) println("Consumer B received: $x")
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel2)
    val consumerA = ConsumerA(channel1)
    val consumerB = ConsumerB(channel2)
    
    launch { produceA(producerA.channel) }
    launch { produceB(producerB.channel) }
    launch { consumeA(consumerA.channel) }
    launch { consumeB(consumerB.channel) }
}