/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test349
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
}

suspend fun functionOne(tc: TestClass) {
    tc.ch1.send(1)
    tc.ch2.send(tc.ch1.receive())
    tc.ch3.send(tc.ch2.receive())
    tc.ch4.send(tc.ch3.receive())
}

suspend fun functionTwo(tc: TestClass) {
    tc.ch5.send(tc.ch4.receive())
    tc.ch6.send(tc.ch5.receive())
    tc.ch7.send(tc.ch6.receive())
    tc.ch1.send(tc.ch7.receive())
}

fun main(): Unit = runBlocking {
    val tc = TestClass()

    launch {
        functionOne(tc)
    }

    launch {
        functionTwo(tc)
    }

    launch {
        tc.ch1.receive()
    }
}

class RunChecker349: RunCheckerBase() {
    override fun block() = main()
}