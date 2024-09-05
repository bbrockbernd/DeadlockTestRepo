/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.generated.test978
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            outChannel.send(it)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel: Channel<Int>) {
    suspend fun consume() {
        for (value in inChannel) {
            println("Consumed: $value")
        }
    }
}

class Processor(private val inChannel: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun process() {
        for (value in inChannel) {
            outChannel.send(value * 2)
        }
        outChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        Producer(channel1).produce()
    }

    launch {
        Processor(channel1, channel2).process()
    }

    launch {
        Consumer(channel2).consume()
    }

    coroutineScope {
        launch {
            delay(1000L)
            println("All operations completed without deadlock")
        }
    }
}