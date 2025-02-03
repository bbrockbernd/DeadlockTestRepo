/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 6 different coroutines
- 4 different classes

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
package org.example.altered.test16
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer1(val ch: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            ch.send(i)
        }
        ch.close()
    }
}

class Producer2(val ch: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            ch.send(i)
        }
        ch.close()
    }
}

class Consumer1(val ch: Channel<Int>, val output: Channel<Int>) {
    suspend fun consume() {
        for (value in ch) {
            output.send(value * 2)
        }
        output.close()
    }
}

class Consumer2(val ch: Channel<Int>, val output: Channel<Int>) {
    suspend fun consume() {
        for (value in ch) {
            output.send(value + 1)
        }
        output.close()
    }
}

fun processIntermediate(ch: Channel<Int>, output: Channel<Int>) {
    GlobalScope.launch {
        for (value in ch) {
            output.send(value - 1)
        }
        output.close()
    }
}

fun printResults(ch: Channel<Int>) {
    GlobalScope.launch {
        for (value in ch) {
            println(value)
        }
    }
}

suspend fun main(): Unit= coroutineScope {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val producer1 = Producer1(ch1)
    val producer2 = Producer2(ch2)
    val consumer1 = Consumer1(ch1, ch3)
    val consumer2 = Consumer2(ch2, ch4)

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { consumer1.consume() }
    launch { consumer2.consume() }

    processIntermediate(ch3, ch5)
    processIntermediate(ch4, ch5)
    printResults(ch5)
}

class RunChecker16: RunCheckerBase() {
    override fun block() = main()
}