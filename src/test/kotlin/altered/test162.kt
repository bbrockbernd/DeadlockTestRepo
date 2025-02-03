/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test162
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel: Channel<Int>, private val resultChannel: Channel<Int>) {
    suspend fun consume() {
        for (item in inChannel) {
            resultChannel.send(item * 2)
        }
        resultChannel.close()
    }
}

fun forwarding(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        for (item in channel1) {
            channel2.send(item)
        }
        channel2.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel3, channel5)

    launch {
        producer.produce()
    }
    
    forwarding(channel1, channel2)
    forwarding(channel2, channel3)

    launch {
        consumer.consume()
    }

    for (item in channel5) {
        println("Received: $item")
    }
}

class RunChecker162: RunCheckerBase() {
    override fun block() = main()
}