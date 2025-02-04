/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test482
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>(2)
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>(2)
    val channel4 = Channel<Int>(2)
    val channel5 = Channel<Int>(2)
    val channel6 = Channel<Int>(2)
    val channel7 = Channel<Int>(2)

    suspend fun function1() {
        channel1.send(1)
        channel2.send(2)
        channel3.send(3)

        channel4.receive()
        channel5.receive()
    }

    suspend fun function2() {
        channel4.send(4)
        channel5.send(5)
        channel6.send(6)

        channel2.receive()
        channel7.receive()
    }

    suspend fun function3() {
        channel7.send(7)
        channel1.receive()

        channel3.receive()
        channel6.receive()
    }
}

fun main(): Unit= runBlocking {
    val deadlockExample = DeadlockExample()

    launch {
        deadlockExample.function1()
    }

    launch {
        deadlockExample.function2()
    }

    launch {
        deadlockExample.function3()
    }
}

class RunChecker482: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}