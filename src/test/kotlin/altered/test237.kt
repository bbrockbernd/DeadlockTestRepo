/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 1 different coroutines
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
package org.example.altered.test237
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Transformer(private val inChannel: Channel<Int>, private val outChannel1: Channel<Int>, private val outChannel2: Channel<Int>) {
    suspend fun transform() {
        for (item in inChannel) {
            outChannel1.send(item * 2)
            outChannel2.send(item * 3)
        }
        outChannel1.close()
        outChannel2.close()
    }
}

class Consumer(private val inChannel1: Channel<Int>, private val inChannel2: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun consume() {
        for (item in inChannel1) {
            outChannel.send(item)
        }
        for (item in inChannel2) {
            outChannel.send(item)
        }
        outChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val producerChannel = Channel<Int>()
    val transformerChannel1 = Channel<Int>()
    val transformerChannel2 = Channel<Int>()
    val consumerChannel1 = Channel<Int>()
    val consumerChannel2 = Channel<Int>()
    val finalChannel = Channel<Int>()

    val producer = Producer(producerChannel)
    val transformer = Transformer(producerChannel, transformerChannel1, transformerChannel2)
    val consumer = Consumer(transformerChannel1, transformerChannel2, finalChannel)

    launch { producer.produce() }
    launch { transformer.transform() }
    launch { consumer.consume() }

    for (item in finalChannel) {
        println("Received: $item")
    }
}

class RunChecker237: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}