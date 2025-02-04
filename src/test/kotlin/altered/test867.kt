/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test867
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel: Channel<Int>) {
    suspend fun process(value: Int) {
        channel.send(value)
    }

    fun calculate(value: Int): Int {
        return value * 2
    }

    suspend fun fetch(): Int {
        return channel.receive()
    }

    fun result(value: Int): Int {
        return value + 1
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val processor = Processor(channel)

    launch {
        val input = 5
        val calculated = processor.calculate(input)
        processor.process(calculated)
    }

    val received = processor.fetch()
    val finalResult = processor.result(received)
    println("Final Result: $finalResult")
}

class RunChecker867: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}