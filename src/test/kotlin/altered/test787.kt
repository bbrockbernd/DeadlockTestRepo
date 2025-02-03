/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test787
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker(val id: Int)

suspend fun sendToChannelA(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun sendToChannelB(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun receiveFromChannelA(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun receiveFromChannelB(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun workerRoutine(worker: Worker, chanA: Channel<Int>, chanB: Channel<Int>, chanC: Channel<Int>, chanD: Channel<Int>) {
    val valueA = receiveFromChannelA(chanA)
    sendToChannelB(chanB, valueA + worker.id)
    val valueB = receiveFromChannelB(chanB)
    sendToChannelA(chanC, valueB * worker.id)
    val valueC = receiveFromChannelA(chanC)
    sendToChannelB(chanD, valueC - worker.id)
}

fun main(): Unit= runBlocking {
    val chanA = Channel<Int>()
    val chanB = Channel<Int>()
    val chanC = Channel<Int>()
    val chanD = Channel<Int>()

    val worker = Worker(1)

    launch { sendToChannelA(chanA, 10) }
    launch { workerRoutine(worker, chanA, chanB, chanC, chanD) }
    launch { sendToChannelB(chanB, 20) }
    launch { receiveFromChannelA(chanC) }
    launch {
        val finalValue = receiveFromChannelB(chanD)
        println("Final value: $finalValue")
    }
}

class RunChecker787: RunCheckerBase() {
    override fun block() = main()
}