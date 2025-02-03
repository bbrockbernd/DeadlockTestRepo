/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
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
package org.example.altered.test196
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val ch1: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            ch1.send(i)
        }
    }
}

class Producer2(val ch2: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            ch2.send(i)
        }
    }
}

class Consumer1(val ch3: Channel<Int>) {
    suspend fun consume(): Int {
        return ch3.receive()
    }
}

class Consumer2(val ch4: Channel<Int>) {
    suspend fun consume(): Int {
        return ch4.receive()
    }
}

fun channelTransfer(ch1: Channel<Int>, ch5: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                ch5.send(ch1.receive())
            }
        }
    }
}

fun crossTransfer(ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                ch3.send(ch2.receive())
                ch4.send(ch3.receive())
            }
        }
    }
}

fun monitor(ch5: Channel<Int>, ch4: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                println(ch5.receive())
                println(ch4.receive())
            }
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    runBlocking {
        launch {
            Producer1(ch1).produce()
        }
        launch {
            Producer2(ch2).produce()
        }
        launch {
            Consumer1(ch3).consume()
        }
        launch {
            Consumer2(ch4).consume()
        }
        launch {
            channelTransfer(ch1, ch5)
        }
        launch {
            crossTransfer(ch2, ch3, ch4)
        }
        monitor(ch5, ch4)
    }
}

class RunChecker196: RunCheckerBase() {
    override fun block() = main()
}