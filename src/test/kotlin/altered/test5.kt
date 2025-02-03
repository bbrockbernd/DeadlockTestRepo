/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test5
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Transformer {
    suspend fun transform(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
        for (value in inputChannel) {
            outputChannel.send(value * 2)
        }
        outputChannel.close()
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>): List<Int> {
        val result = mutableListOf<Int>()
        for (value in channel) {
            result.add(value)
        }
        return result
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = Producer()
    val transformer = Transformer()
    val consumer = Consumer()

    launch {
        producer.produce(channel1)
    }

    launch {
        transformer.transform(channel1, channel2)
    }

    val result = consumer.consume(channel2)
    println(result)
}

class RunChecker5: RunCheckerBase() {
    override fun block() = main()
}