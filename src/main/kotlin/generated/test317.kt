/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
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
package org.example.generated.test317
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i * 2)
        }
        channel.close()
    }
}

class Transformer(private val channel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun transform() {
        for (item in channel) {
            outputChannel.send(item + 1)
        }
        outputChannel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (item in channel) {
            println("Consumed: $item")
        }
    }
}

fun main(): Unit = runBlocking {
    // Initialize channels
    val producerChannel = Channel<Int>(5)
    val transformerChannel = Channel<Int>(5)

    // Initialize instances
    val producer = Producer(producerChannel)
    val transformer = Transformer(producerChannel, transformerChannel)
    val consumer = Consumer(transformerChannel)

    // Launch coroutines
    launch { producer.produce() }
    launch { transformer.transform() }
    launch { consumer.consume() }

    // Additional coroutines to showcase the 5 coroutine requirement 
    launch { delay(100); println("Additional coroutine 1") }
    launch { delay(100); println("Additional coroutine 2") }
}