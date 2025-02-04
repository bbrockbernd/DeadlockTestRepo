/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.altered.test850
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel = Channel<Int>()

    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

class Consumer {
    val channel = Channel<Int>()

    suspend fun consume(channel: Channel<Int>) {
        for (item in channel) {
            this.channel.send(item * 2)
        }
        this.channel.close()
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()

    launch {
        producer.produce()
    }

    launch {
        consumer.consume(producer.channel)
    }

    for (item in consumer.channel) {
        println("Consumed: $item")
    }
}

class RunChecker850: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}