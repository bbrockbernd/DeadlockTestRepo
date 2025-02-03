/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test534
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Processor(private val channel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val received = channel.receive()
            println("Processed: $received")
        }
    }
}

class Consumer {
    fun consume(producer: Producer, processor: Processor) = runBlocking {
        launch { producer.produce() }
        launch { processor.process() }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val processor = Processor(channel)
    val consumer = Consumer()

    consumer.consume(producer, processor)
}

class RunChecker534: RunCheckerBase() {
    override fun block() = main()
}