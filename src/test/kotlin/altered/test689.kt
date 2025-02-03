/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
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
package org.example.altered.test689
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val sendChannel1: Channel<Int>, private val sendChannel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Producing: $i")
            sendChannel1.send(i)
            sendChannel2.send(i * 2)
        }
        sendChannel1.close()
        sendChannel2.close()
    }
}

class Consumer(private val receiveChannel1: Channel<Int>, private val receiveChannel2: Channel<Int>, private val sendChannel: Channel<Int>) {
    suspend fun consume() {
        for (msg1 in receiveChannel1) {
            println("Consuming from Channel 1: $msg1")
            sendChannel.send(msg1)
        }

        for (msg2 in receiveChannel2) {
            println("Consuming from Channel 2: $msg2")
            sendChannel.send(msg2)
        }
        sendChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val resultChannel = Channel<Int>()

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel1, channel2, resultChannel)

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    for (result in resultChannel) {
        println("Final result: $result")
    }
}

class RunChecker689: RunCheckerBase() {
    override fun block() = main()
}