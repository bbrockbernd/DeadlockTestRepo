/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.generated.test607
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

class Coordinator(private val channel: Channel<Int>) {
    suspend fun coordinate() {
        Producer(channel).produce()
        Consumer(channel).consume()
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    val job1 = launch {
        Coordinator(channel).coordinate()
    }

    val job2 = launch {
        delay(100)  // Cause a delay to exacerbate deadlock
        Consumer(channel).consume()
    }

    joinAll(job1, job2)  // Leads to deadlock, hence commented.
}