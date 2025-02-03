/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test969
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class ProducerB {
    suspend fun produce(channel: Channel<String>) {
        val messages = listOf("First", "Second", "Third", "Fourth", "Fifth")
        for (message in messages) {
            channel.send(message)
        }
        channel.close()
    }
}

class Consumer {
    suspend fun consume(channel1: Channel<Int>, channel2: Channel<String>, channel3: Channel<Double>, channel4: Channel<Boolean>) {
        launch { 
            for (value in channel1) { 
                println("Channel1: $value")
                channel3.send(value.toDouble())
            } 
        }
        launch { 
            for (message in channel2) { 
                println("Channel2: $message")
                channel4.send(true)
            } 
        }
    }
}

fun main(): Unit= runBlocking<Unit> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Double>()
    val channel4 = Channel<Boolean>()

    val producerA = ProducerA()
    val producerB = ProducerB()
    val consumer = Consumer()

    launch {
        producerA.produce(channel1)
    }
    launch {
        producerB.produce(channel2)
    }
    consumer.consume(channel1, channel2, channel3, channel4)

    launch { 
        for (result in channel3) { 
            println("Channel3: $result")
        } 
    }
    launch { 
        for (flag in channel4) { 
            println("Channel4: $flag")
        } 
    }
}

class RunChecker969: RunCheckerBase() {
    override fun block() = main()
}