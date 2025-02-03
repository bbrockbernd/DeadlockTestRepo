/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test283
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun produce1() {
        for (x in 1..5) {
            ch1.send(x)
            ch2.send(x * 2)
        }
    }
}

class Producer2(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun produce2() {
        for (x in 6..10) {
            ch3.send(x)
            ch4.send(x * 2)
        }
    }
}

suspend fun consume1(ch1: Channel<Int>, ch5: Channel<Int>) {
    for (x in 1..5) {
        val value = ch1.receive()
        ch5.send(value + 10)
    }
}

suspend fun consume2(ch2: Channel<Int>, ch6: Channel<Int>) {
    for (x in 1..5) {
        val value = ch2.receive()
        ch6.send(value + 20)
    }
}

suspend fun consume3(ch3: Channel<Int>, ch7: Channel<Int>) {
    for (x in 6..10) {
        val value = ch3.receive()
        ch7.send(value + 30)
    }
}

suspend fun consume4(ch4: Channel<Int>, ch8: Channel<Int>) {
    for (x in 6..10) {
        val value = ch4.receive()
        ch8.send(value + 40)
    }
}

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val producer1 = Producer1(ch1, ch2)
    val producer2 = Producer2(ch3, ch4)
    
    launch { producer1.produce1() }
    launch { producer2.produce2() }
    launch { consume1(ch1, ch5) }
    launch { consume2(ch2, ch6) }
    launch { consume3(ch3, ch7) }
    
    coroutineScope {
        consume4(ch4, ch8)
    }
}

class RunChecker283: RunCheckerBase() {
    override fun block() = main()
}