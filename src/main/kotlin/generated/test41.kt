/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
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
package org.example.generated.test41
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val outputChannel1: Channel<Int>, val outputChannel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outputChannel1.send(i)
            outputChannel2.send(i * 2)
        }
        outputChannel1.close()
        outputChannel2.close()
    }
}

class Consumer(val inputChannel1: Channel<Int>, val inputChannel2: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in inputChannel1) {
            outputChannel.send(i)
        }
        for (i in inputChannel2) {
            outputChannel.send(i)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val producerOutputChannel1 = Channel<Int>()
    val producerOutputChannel2 = Channel<Int>()
    val consumerInputChannel1 = Channel<Int>()
    val consumerInputChannel2 = Channel<Int>()
    val finalOutputChannel = Channel<Int>()

    val producer = Producer(producerOutputChannel1, producerOutputChannel2)
    val consumer = Consumer(consumerInputChannel1, consumerInputChannel2, finalOutputChannel)

    launch {
        producer.produce()
    }

    launch {
        for (i in producerOutputChannel1 + producerOutputChannel2) {
            consumerInputChannel1.send(i)
            consumerInputChannel2.send(i)
        }
        consumerInputChannel1.close()
        consumerInputChannel2.close()
    }

    launch {
        consumer.consume()
    }

    for (i in finalOutputChannel) {
        println(i)
    }
}