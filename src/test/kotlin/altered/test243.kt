/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.altered.test243
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i) // Suspending function call
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println(channel.receive()) // Suspending function call
        }
    }
}

fun mainFunction() {
    val firstChannel = Channel<Int>()
    val secondChannel = Channel<Int>()
    val thirdChannel = Channel<Int>()

    val producer = Producer(firstChannel)
    val consumer = Consumer(secondChannel)

    runBlocking {
        launch {
            producer.produce() // Suspends to send values to firstChannel
            secondChannel.send(firstChannel.receive()) // Deadlock point: This suspends waiting for a value from firstChannel, which producer has not sent yet as it is blocked.
        }

        launch {
            consumer.consume() // Suspends waiting to receive from secondChannel
            thirdChannel.send(secondChannel.receive()) // Deadlock point: This suspends waiting for a value from secondChannel 
        }

        launch {
            println(thirdChannel.receive()) // Deadlock point: This suspends waiting to receive from thirdChannel
        }
    }
}

fun main(): Unit{
    mainFunction()
}

class RunChecker243: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}