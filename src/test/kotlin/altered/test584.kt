/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test584
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>()

    suspend fun produceValues() {
        for (i in 1..5) {
            sendValue(i)
        }
    }

    suspend fun sendValue(value: Int) {
        channel.send(value)
    }

    suspend fun consumeValues() {
        repeat(5) {
            println("Received: ${receiveValue()}")
        }
    }

    suspend fun receiveValue(): Int {
        return channel.receive()
    }

    fun closeChannel() {
        channel.close()
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()

    launch {
        processor.produceValues()
    }

    launch {
        processor.consumeValues()
    }

    launch {
        delay(1000)
        processor.closeChannel()
    }
}

class RunChecker584: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}