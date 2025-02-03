/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 5 different coroutines
- 3 different classes

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
package org.example.altered.test994
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer1(val channel: Channel<String>) {
    suspend fun produce() {
        channel.send("Message from Producer1")
    }
}

class Producer2(val channel: Channel<String>) {
    suspend fun produce() {
        channel.send("Message from Producer2")
    }
}

class Consumer(val channel1: Channel<String>, val channel2: Channel<String>, val channel3: Channel<String>) {
    suspend fun consume() {
        coroutineScope {
            channel1.receive()
            channel2.receive()
            channel3.send("Message to Channel3")
        }
    }
}

fun mainProducer(channel: Channel<String>): suspend () -> Unit = {
    channel.send("Message from mainProducer")
}

suspend fun mainConsumer(channel: Channel<String>) {
    channel.receive()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val consumer = Consumer(channel1, channel2, channel5)

    launch {
        producer1.produce()
    }
    
    launch {
        producer2.produce()
    }
    
    launch {
        consumer.consume()
    }
    
    launch {
        mainProducer(channel3)()
    }
    
    launch {
        mainConsumer(channel4) 
    }
}

main()

class RunChecker994: RunCheckerBase() {
    override fun block() = main()
}