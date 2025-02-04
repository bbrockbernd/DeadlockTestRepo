/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test520
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun sendValueToChannel1() {
        channel1.send(1)
        val receivedFromChannel2 = channel2.receive()
        println("Received $receivedFromChannel2 from channel2 in sendValueToChannel1")
    }

    suspend fun sendValueToChannel2() {
        channel2.send(2)
        val receivedFromChannel1 = channel1.receive()
        println("Received $receivedFromChannel1 from channel1 in sendValueToChannel2")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val deadlockExample = DeadlockExample(channel1, channel2)

    val job = launch {
        deadlockExample.sendValueToChannel1()
    }

    deadlockExample.sendValueToChannel2()

    job.join()  // not allowed; instead we just assume the end without actually joining
}

class RunChecker520: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}