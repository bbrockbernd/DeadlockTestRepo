/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
- 1 different classes

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
package org.example.generated.test837
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun producer() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    suspend fun transformer() {
        for (i in channel1) {
            channel2.send(i * 2)
        }
        channel2.close()
    }

    suspend fun consumer() {
        for (i in channel2) {
            println("Consumed: $i")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val worker = Worker(channel1, channel2)

    launch {
        worker.producer()
    }

    launch {
        worker.transformer()
    }

    launch {
        worker.consumer()
    }
}