/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 2 different coroutines
- 0 different classes

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
package org.example.altered.test884
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Double>()

    launchProducer1(channel1)
    launchProducer2(channel2)
    launchConsumer(channel1, channel2, channel3)
    printChannelValues(channel3)
}

suspend fun launchProducer1(channel: Channel<Int>) = coroutineScope {
    launch {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

suspend fun launchProducer2(channel: Channel<String>) = coroutineScope {
    launch {
        val messages = listOf("A", "B", "C", "D", "E")
        for (msg in messages) {
            channel.send(msg)
        }
        channel.close()
    }
}

suspend fun launchConsumer(channel1: Channel<Int>, channel2: Channel<String>, channel3: Channel<Double>) = coroutineScope {
    launch {
        val int1 = channel1.receive()
        val int2 = channel1.receive()
        val str1 = channel2.receive()
        val str2 = channel2.receive()
        channel3.send(processValues(int1, int2, str1, str2))
        channel3.close()
    }
}

fun processValues(int1: Int, int2: Int, str1: String, str2: String): Double {
    return (int1 + int2) * str1.length + str2.length
}

suspend fun printChannelValues(channel: Channel<Double>) = coroutineScope {
    launch {
        for (value in channel) {
            println("Received value: $value")
        }
    }
}

class RunChecker884: RunCheckerBase() {
    override fun block() = main()
}