/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
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
package org.example.altered.test988
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        channel.send(1)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        channel.receive()
    }
}

class Processor(private val input: Channel<Int>, private val output: Channel<Int>) {
    suspend fun process() {
        val item = input.receive()
        output.send(item)
    }
}

suspend fun deadlockExample(producerChannel: Channel<Int>, processorChannel: Channel<Int>) {
    val producer = Producer(producerChannel)
    val consumer = Consumer(producerChannel)
    val processor = Processor(producerChannel, processorChannel)

    coroutineScope {
        launch { producer.produce() }
        launch { producer.produce() }
        launch { processor.process() }
        launch { consumer.consume() }
        launch { processor.process() }
    }
}

fun main(): Unit= runBlocking {
    val producerChannel = Channel<Int>()
    val processorChannel = Channel<Int>()

    deadlockExample(producerChannel, processorChannel)
}

class RunChecker988: RunCheckerBase() {
    override fun block() = main()
}