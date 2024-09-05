/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.generated.test810
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val outputChannel: Channel<Int>) {
    suspend fun produce(start: Int, end: Int) {
        for (i in start until end) {
            outputChannel.send(i)
        }
        outputChannel.close()
    }
}

class Consumer(private val inputChannel: Channel<Int>) {
    suspend fun consume(resultChannel: Channel<Int>) {
        for (value in inputChannel) {
            resultChannel.send(value * value)
        }
        resultChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(Channel.BUFFERED)
    val channel2 = Channel<Int>(Channel.BUFFERED)
    val channel3 = Channel<Int>(Channel.BUFFERED)

    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)
    val consumer1 = Consumer(channel1)
    val consumer2 = Consumer(channel2)

    launch {
        producer1.produce(1, 10)
    }

    launch {
        producer2.produce(11, 20)
    }

    launch {
        consumer1.consume(channel3)
    }
    
    launch {
        consumer2.consume(channel3)
    }

    coroutineScope {
        for (result in channel3) {
            println(result)
        }
    }
}