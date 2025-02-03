/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test555
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>) = runBlocking {
    launch {
        val value1 = ch1.receive()
        ch2.send(value1 + 1)
        ch4.send(value1 + 3)
        
        val value2 = ch3.receive()
        ch5.send(value2 + value1)
        ch4.send(value2)
    }

    launch {
        val value3 = ch2.receive()
        ch3.send(value3 * 2)
    }
}

fun functionB(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>) = runBlocking {
    launch {
        val value1 = ch4.receive()
        ch1.send(value1 - 3)

        val value2 = ch5.receive()
        ch2.send(value2 / 2)
    }

    launch {
        val value3 = ch5.receive()
        ch4.send(value3 + 4)
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    functionA(ch1, ch2, ch3, ch4, ch5)
    functionB(ch1, ch2, ch3, ch4, ch5)
}

class RunChecker555: RunCheckerBase() {
    override fun block() = main()
}