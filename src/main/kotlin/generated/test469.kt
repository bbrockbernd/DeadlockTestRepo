/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 4 different coroutines
- 4 different classes

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
package org.example.generated.test469
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

// Channels
val channel1 = Channel<Int>()
val channel2 = Channel<String>()
val channel3 = Channel<Double>()

class Producer1 {
    fun produce() = produceNumbers()
    suspend fun produceNumbers() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }
}

class Producer2 {
    fun produce() = produceStrings()
    suspend fun produceStrings() {
        val strings = listOf("A", "B", "C", "D", "E")
        for (str in strings) {
            channel2.send(str)
        }
        channel2.close()
    }
}

class Producer3 {
    fun produce() = produceDoubles()
    suspend fun produceDoubles() {
        for (i in 1..5) {
            channel3.send(i.toDouble() * 1.5)
        }
        channel3.close()
    }
}

class Consumer {
    suspend fun consume() {
        coroutineScope {
            launch {
                for (value in channel1) {
                    println("Received from channel1: $value")
                }
            }
            launch {
                for (value in channel2) {
                    println("Received from channel2: $value")
                }
            }
            launch {
                for (value in channel3) {
                    println("Received from channel3: $value")
                }
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val producer1 = Producer1()
    val producer2 = Producer2()
    val producer3 = Producer3()

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { producer3.produce() }

    val consumer = Consumer()
    consumer.consume()
}