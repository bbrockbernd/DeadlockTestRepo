/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
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
package org.example.altered.test62
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor {
    suspend fun process1(channel1: Channel<Int>, channel2: Channel<Int>) {
        val value = channel1.receive()
        channel2.send(value)
    }

    suspend fun process2(channel2: Channel<Int>, channel3: Channel<Int>) {
        val value = channel2.receive()
        channel3.send(value)
    }
}

suspend fun action1(channel4: Channel<String>, channel5: Channel<String>) {
    val value = channel4.receive()
    channel5.send(value)
}

suspend fun action2(channel5: Channel<String>, channel6: Channel<String>) {
    val value = channel5.receive()
    channel6.send(value)
}

suspend fun action3(channel6: Channel<String>, channel1: Channel<Int>) {
    val strValue = channel6.receive()
    val intValue = strValue.toInt()
    channel1.send(intValue)
}

suspend fun job1(channel1: Channel<Int>, channel7: Channel<Int>) {
    val value = channel1.receive()
    channel7.send(value)
}

suspend fun job2(channel7: Channel<Int>, channel3: Channel<Int>) {
    val value = channel7.receive()
    channel3.send(value)
}

suspend fun job3(channel3: Channel<Int>) {
    val value = channel3.receive()
    println("Final value: $value")
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()
    val channel6 = Channel<String>()
    val channel7 = Channel<Int>()

    val processor = Processor()

    launch { processor.process1(channel1, channel2) }
    launch { processor.process2(channel2, channel3) }
    launch { action1(channel4, channel5) }
    launch { action2(channel5, channel6) }
    launch { action3(channel6, channel1) }
    launch { job1(channel1, channel7) }
    launch { job2(channel7, channel3) }
    launch { job3(channel3) }

    channel4.send("123")
}

class RunChecker62: RunCheckerBase() {
    override fun block() = main()
}