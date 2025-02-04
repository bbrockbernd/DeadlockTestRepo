/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.altered.test449
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel = Channel<Int>()

    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

class Processor {
    fun process(input: Int): Int {
        return input * 2
    }
}

class Consumer {
    suspend fun consumeData(channel: Channel<Int>) {
        for (value in channel) {
            println("Received: $value")
        }
    }
}

fun funcA(producer: Producer) {
    runBlocking {
        launch {
            producer.produceData()
        }
    }
}

fun funcB(processor: Processor, input: Int): Int {
    return processor.process(input)
}

fun funcC(input: Int): Int {
    return input + 10
}

fun funcD(consumer: Consumer, channel: Channel<Int>) {
    runBlocking {
        launch {
            consumer.consumeData(channel)
        }
    }
}

fun funcE(channel: Channel<Int>) {
    runBlocking {
        launch {
            for (i in channel) {
                println("Processed: ${funcC(i)}")
            }
        }
    }
}

fun funcF() = runBlocking {
    val producer = Producer()
    val processor = Processor()
    val consumer = Consumer()

    funcA(producer)
    funcD(consumer, producer.channel)
}

fun main(): Unit= runBlocking {
    funcF()
}

class RunChecker449: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}