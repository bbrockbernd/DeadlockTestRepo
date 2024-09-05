/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.generated.test428
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
        }
    }
}

class Consumer(val input: Channel<Int>, val output: Channel<String>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = input.receive()
            output.send("Consumed $value")
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel1, channel2)

    runBlocking {
        launch {
            producer.produce()
        }
        launch {
            consumer.consume()
        }
        launch {
            for (i in 1..5) {
                val message = channel2.receive()
                channel3.send(message)
            }
        }

        for (i in 1..5) {
            println("Result: ${channel3.receive()}")
        }
    }
}