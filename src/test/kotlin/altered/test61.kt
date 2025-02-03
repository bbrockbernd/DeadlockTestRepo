/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 7 different coroutines
- 5 different classes

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
package org.example.altered.test61
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (value in inputChannel) {
            outputChannel.send(value * 2)
        }
    }
}

class B(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun transform() {
        for (value in inputChannel) {
            outputChannel.send(value + 3)
        }
    }
}

class C(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun filter() {
        for (value in inputChannel) {
            if (value % 2 == 0) {
                outputChannel.send(value)
            }
        }
    }
}

class D(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun subtract() {
        for (value in inputChannel) {
            outputChannel.send(value - 1)
        }
    }
}

class E(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun multiply() {
        for (value in inputChannel) {
            outputChannel.send(value * 1)
        }
    }
}

fun generateNumbers(outputChannel: Channel<Int>) {
    runBlocking {
        launch {
            for (i in 1..10) {
                outputChannel.send(i)
            }
            outputChannel.close()
        }
    }
}

fun consumeResults(inputChannel: Channel<Int>) {
    runBlocking {
        launch {
            for (value in inputChannel) {
                println("Result: $value")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(Channel.BUFFERED)
    val channel2 = Channel<Int>(Channel.BUFFERED)
    val channel3 = Channel<Int>(Channel.BUFFERED)
    val channel4 = Channel<Int>(Channel.BUFFERED)
    val channel5 = Channel<Int>(Channel.BUFFERED)
    val channel6 = Channel<Int>(Channel.BUFFERED)
    val channel7 = Channel<Int>(Channel.BUFFERED)

    val a = A(channel1, channel2)
    val b = B(channel2, channel3)
    val c = C(channel3, channel4)
    val d = D(channel4, channel5)
    val e = E(channel5, channel6)

    coroutineScope {
        launch { a.process() }
        launch { b.transform() }
        launch { c.filter() }
        launch { d.subtract() }
        launch { e.multiply() }
        launch { generateNumbers(channel1) }
        launch { consumeResults(channel6) }
    }
}

class RunChecker61: RunCheckerBase() {
    override fun block() = main()
}