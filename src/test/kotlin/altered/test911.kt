/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
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
package org.example.altered.test911
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        channel.send(1)  // Sends value 1 to the channel
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>) {
        channel.receive()  // Tries to receive a value from the channel
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val producer = Producer()
    val consumer = Consumer()

    // Coroutine 1: Producer sends a value to the channel
    launch {
        producer.produce(channel)
    }

    // Coroutine 2: Consumer tries to receive a value from the channel
    launch {
        consumer.consume(channel)
    }
}

class RunChecker911: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}