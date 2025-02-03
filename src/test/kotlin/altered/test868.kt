/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 4 different coroutines
- 3 different classes

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
package org.example.altered.test868
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(private val ch1: Channel<Int>, private val ch2: Channel<Int>) {
    suspend fun process() {
        launch {
            repeat(3) {
                ch1.send(it)
            }
        }

        launch {
            repeat(3) {
                ch2.send(it + 10)
            }
        }
    }
}

class B(private val ch3: Channel<String>, private val ch4: Channel<String>) {
    fun convertAndSend() {
        runBlocking {
            coroutineScope {
                launch {
                    ch3.send("Hello")
                }
                launch {
                    ch4.send("World")
                }
            }
        }
    }
}

class C(private val ch5: Channel<Double>) {
    fun multiply(value: Double) {
        runBlocking {
            ch5.send(value * 2)
        }
    }
}

fun mainChannelUsage(ch: Channel<Int>) {
    runBlocking {
        while (true) {
            println("Received from channel: ${ch.receive()}")
        }
    }
}

fun additionalWork(ch5: Channel<Double>) {
    runBlocking {
        while (true) {
            println("Received double value: ${ch5.receive()}")
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<String>()
    val ch4 = Channel<String>()
    val ch5 = Channel<Double>()

    val a = A(ch1, ch2)
    val b = B(ch3, ch4)
    val c = C(ch5)

    runBlocking {
        a.process()
        b.convertAndSend()
        
        launch {
            mainChannelUsage(ch1)
        }

        launch {
            mainChannelUsage(ch2)
        }
        
        launch {
            additionalWork(ch5)
        }

        c.multiply(5.0)
    }
}

class RunChecker868: RunCheckerBase() {
    override fun block() = main()
}