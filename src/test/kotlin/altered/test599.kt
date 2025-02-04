/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test599
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(2)
    val channel5 = Channel<Int>()

    suspend fun sender() {
        channel1.send(1)
        channel2.send(2)
        channel3.send(3)
        channel4.send(4)
        channel5.send(5)
    }

    suspend fun receiver() {
        println(channel1.receive())
        println(channel2.receive())
        println(channel3.receive())
        println(channel4.receive())
        println(channel5.receive())
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()

    launch {
        processor.sender()
    }

    launch {
        processor.receiver()
    }
}

class RunChecker599: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}