/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 4 different coroutines
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
package org.example.generated.test28
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    suspend fun processA(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) {
        channel1.send(1)
        channel2.send(2)
        val a = channel3.receive()
        val b = channel4.receive()
    }

    suspend fun processB(channel5: Channel<Int>, channel6: Channel<Int>, channel7: Channel<Int>, channel8: Channel<Int>) {
        channel5.send(5)
        channel6.send(6)
        val x = channel7.receive()
        val y = channel8.receive()
    }
}

class Executor {
    val processor = Processor()

    fun exec() = runBlocking {
        val channel1 = Channel<Int>()
        val channel2 = Channel<Int>()
        val channel3 = Channel<Int>()
        val channel4 = Channel<Int>()
        val channel5 = Channel<Int>()
        val channel6 = Channel<Int>()
        val channel7 = Channel<Int>()
        val channel8 = Channel<Int>()

        launch {
            processor.processA(channel1, channel2, channel3, channel4)
        }

        launch {
            processor.processB(channel5, channel6, channel7, channel8)
        }

        launch {
            // Create a circular dependency causing the deadlock intentionally
            channel4.send(channel5.receive())
        }

        launch {
            channel8.send(channel3.receive())
        }
    }
}

fun main(): Unit{
    val executor = Executor()
    executor.exec()
}