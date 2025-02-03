/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test902
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

class Producer1(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
            delay(100)
        }
        outChannel.close()
    }
}

class Producer2(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            outChannel.send(i)
            delay(150)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel1: Channel<Int>, private val inChannel2: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun consume() {
        for (element1 in inChannel1) {
            outChannel.send(element1 * 2)
        }
        for (element2 in inChannel2) {
            outChannel.send(element2 * 2)
        }
        outChannel.close()
    }
}

suspend fun relay(from: Channel<Int>, to: Channel<Int>) {
    for (element in from) {
        to.send(element)
    }
    to.close()
}

fun main(): Unit= runBlocking {
    val producerChannel1 = Channel<Int>()
    val producerChannel2 = Channel<Int>()
    val consumerChannel1 = Channel<Int>()
    val consumerChannel2 = Channel<Int>()
    val resultChannel = Channel<Int>()

    val producer1 = Producer1(producerChannel1)
    val producer2 = Producer2(producerChannel2)
    val consumer = Consumer(consumerChannel1, consumerChannel2, resultChannel)

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { relay(producerChannel1, consumerChannel1) }
    launch { relay(producerChannel2, consumerChannel2) }
    launch { consumer.consume() }

    for (result in resultChannel) {
        println(result)
    }
}

class RunChecker902: RunCheckerBase() {
    override fun block() = main()
}