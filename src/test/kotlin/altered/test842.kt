/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test842
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class CoroutineWorker1(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun receiveAndSend() {
        val received = channel1.receive()
        channel2.send(received)
    }
}

class CoroutineWorker2(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun sendAndReceive() {
        channel3.send(42)
        val received = channel4.receive()
        println("Received: $received")
    }
}

suspend fun Communication(worker1: CoroutineWorker1, worker2: CoroutineWorker2, channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) {
    coroutineScope {
        launch {
            worker1.receiveAndSend()
        }
        launch {
            worker2.sendAndReceive()
        }
        launch {
            channel4.send(channel3.receive())
        }
        launch {
            channel2.send(channel1.receive())
        }
        launch {
            channel1.send(24)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val worker1 = CoroutineWorker1(channel1, channel2)
    val worker2 = CoroutineWorker2(channel3, channel4)

    Communication(worker1, worker2, channel1, channel2, channel3, channel4)
}

class RunChecker842: RunCheckerBase() {
    override fun block() = main()
}