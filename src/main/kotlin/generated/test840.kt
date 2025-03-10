/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.generated.test840
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    suspend fun produceToA() {
        for (i in 1..5) {
            channelA.send(i)
        }
    }
    suspend fun produceToB() {
        for (i in 6..10) {
            channelB.send(i)
        }
    }
}

class Consumer {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    suspend fun consumeFromA(channel: Channel<Int>) {
        val item = channel.receive()
        channelC.send(item)
    }
    suspend fun consumeFromB(channel: Channel<Int>) {
        val item = channel.receive()
        channelD.send(item)
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    
    launch {
        producer.produceToA()
    }
    launch {
        consumer.consumeFromA(producer.channelA)
    }
    
    coroutineScope {
        delay(100) // Ensure the above coroutines start
        producer.produceToB()
        consumer.consumeFromB(producer.channelB)
    }
}