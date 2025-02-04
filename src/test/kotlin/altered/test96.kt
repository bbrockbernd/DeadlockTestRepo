/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test96
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class MyProcessor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }

    suspend fun receiveFromChannel1(): Int {
        return channel1.receive()
    }

    suspend fun receiveFromChannel2(): Int {
        return channel2.receive()
    }

    suspend fun processValues() {
        val value1 = receiveFromChannel1()
        val value2 = receiveFromChannel2()
        println("Processed values: $value1, $value2")
    }
}

fun producer1(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

fun producer2(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

fun consumer1(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        val processor = MyProcessor(channel1, channel2)
        for (i in 1..5) {
            processor.processValues()
        }
    }
}

fun consumer2(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        val processor = MyProcessor(channel1, channel2)
        for (i in 1..5) {
            processor.processValues()
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    producer1(channel1)
    producer2(channel2)

    launch {
        for (i in 1..5) {
            channel3.send(i)
        }
    }

    consumer1(channel1, channel3)
    consumer2(channel2, channel3)

    delay(1000L)
}

class RunChecker96: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}