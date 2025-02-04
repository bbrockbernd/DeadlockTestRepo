/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test648
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            delay(100)
            channel2.send(i * 2)
        }
    }
}

class Consumer {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    suspend fun consume() {
        repeat(5) {
            val value1 = channel3.receive()
            val value2 = channel4.receive()
            println("Consumed: $value1 and $value2")
        }
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()

    launch {
        for (i in 1..5) {
            val value1 = producer.channel1.receive()
            val value2 = producer.channel2.receive()
            consumer.channel3.send(value1)
            consumer.channel4.send(value2)
        }
    }

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    delay(3000)
    println("Main completed")
}

class RunChecker648: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}