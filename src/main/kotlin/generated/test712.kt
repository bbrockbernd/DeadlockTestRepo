/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test712
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        channel.send(1)
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        channel.receive()
    }
}

class Coordinator(val channel: Channel<Int>) {
    suspend fun coordinate() = coroutineScope {
        val producer = Producer(channel)
        val consumer = Consumer(channel)

        launch { producer.produce() }
        launch { consumer.consume() }

        launch { producer.produce() }
    }
}

suspend fun functionOne(channel: Channel<Int>) = coroutineScope {
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    launch { producer.produce() }
    launch { consumer.consume() }
}

suspend fun functionTwo(channel: Channel<Int>) = coroutineScope {
    val coordinator = Coordinator(channel)
    val consumer = Consumer(channel)

    launch { coordinator.coordinate() }
    launch { consumer.consume() }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { functionOne(channel) }
    launch { functionTwo(channel) }
}