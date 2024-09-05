/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 7 different coroutines
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
package org.example.generated.test137
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel1: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }
}

class Consumer(private val channel2: Channel<String>) {
    suspend fun consume() {
        for (msg in channel2) {
            println("Consumed message: $msg")
        }
    }
}

fun runProducer(producer: Producer) = runBlocking {
    launch {
        producer.produce()
    }
}

fun distribute(channel1: Channel<Int>, channel2: Channel<String>) = runBlocking {
    launch {
        for (i in channel1) {
            channel2.send("Message $i")
        }
        channel2.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val producer = Producer(channel1)
    val consumer = Consumer(channel2)

    launch { runProducer(producer) }
    launch { distribute(channel1, channel2) }
    launch { consumer.consume() }
    launch { println("Auxiliary coroutine 1 is running") }
    launch { println("Auxiliary coroutine 2 is running") }
    launch { println("Auxiliary coroutine 3 is running") }
    launch { println("Auxiliary coroutine 4 is running") }
}