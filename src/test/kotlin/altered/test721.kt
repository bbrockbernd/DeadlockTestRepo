/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test721
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (item in channel) {
            println("Consumed: $item")
        }
    }
}

class Controller(val producer: Producer, val consumer: Consumer) {
    suspend fun control() {
        coroutineScope {
            launch {
                producer.produce()
            }
            launch {
                consumer.consume()
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val producer = Producer(channel1)
    val consumer = Consumer(channel2)
    val controller = Controller(producer, consumer)

    controller.control()
}

class RunChecker721: RunCheckerBase() {
    override fun block() = main()
}