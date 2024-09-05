/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":8,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.generated.test127
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker1(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun doWork() {
        for (i in 1..4) {
            channelA.send(i)
            val received = channelB.receive()
            println("Worker1 received: $received")
        }
    }
}

class Worker2(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun doWork() {
        for (i in 1..4) {
            channelB.send(i)
            val received = channelA.receive()
            println("Worker2 received: $received")
        }
    }
}

fun createCoroutines(worker1: Worker1, worker2: Worker2) = runBlocking {
    repeat(4) {
        launch { worker1.doWork() }
        launch { worker2.doWork() }
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val worker1 = Worker1(channelA, channelB)
    val worker2 = Worker2(channelA, channelB)

    createCoroutines(worker1, worker2)
}