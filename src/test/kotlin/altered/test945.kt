/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test945
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        while (!channel.isClosedForReceive) {
            val value = channel.receiveCatching().getOrNull() ?: break
            println("Consumed $value")
        }
    }
}

class Handler(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun handle() {
        while (!inputChannel.isClosedForReceive) {
            val value = inputChannel.receiveCatching().getOrNull() ?: break
            outputChannel.send(value * 2)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val producerChannel = Channel<Int>()
    val handlerChannel = Channel<Int>()
    val consumerChannel = Channel<Int>()

    val producer = Producer(producerChannel)
    val handler = Handler(producerChannel, handlerChannel)
    val consumer = Consumer(handlerChannel)

    launch {
        producer.produce()
    }
    launch {
        handler.handle()
    }
    launch {
        consumer.consume()
    }

    // Adding an additional coroutine for complexity, but it won't cause a deadlock
    launch {
        delay(500)
        println("Additional coroutine running")
    }
}

class RunChecker945: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}