/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.altered.test680
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val outputChannel = Channel<Int>()

    suspend fun produce(values: List<Int>) {
        for (value in values) {
            outputChannel.send(value)
        }
        outputChannel.close()
    }
}

class Consumer {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()

    suspend fun consume() {
        for (value in inputChannel) {
            outputChannel.send(value * 2)
        }
        outputChannel.close()
    }
}

class Aggregator {
    val inputChannel1 = Channel<Int>()
    val inputChannel2 = Channel<Int>()
    
    suspend fun aggregate(outputChannel: Channel<Int>) {
        val value1 = inputChannel1.receive()
        val value2 = inputChannel2.receive()
        outputChannel.send(value1 + value2)
    }
}

fun main(): Unit= runBlocking {
    val producer1 = Producer()
    val producer2 = Producer()
    val consumer1 = Consumer()
    val consumer2 = Consumer()
    val aggregator = Aggregator()

    launch {
        producer1.produce(listOf(1, 2, 3))
    }

    launch {
        producer2.produce(listOf(4, 5, 6))
    }

    launch {
        consumer1.consume()
    }

    launch {
        consumer2.consume()
    }

    launch {
        // Imposing a deadlock by improper channel dependencies
        consumer1.inputChannel.send(producer1.outputChannel.receive())
        consumer2.inputChannel.send(producer2.outputChannel.receive())
        aggregator.inputChannel1.send(consumer1.outputChannel.receive())
        aggregator.inputChannel2.send(consumer2.outputChannel.receive())
        aggregator.aggregate(Channel())
    }
}

class RunChecker680: RunCheckerBase() {
    override fun block() = main()
}