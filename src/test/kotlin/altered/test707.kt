/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test707
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()

    launch { coroutine1(ch1, ch2, ch3) }
    launch { coroutine2(ch1, ch2, ch4) }
    launch { deadlockCoroutine(ch3, ch4) }
}

suspend fun coroutine1(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>) {
    ch1.send(1)
    ch2.send(2)
    val received = ch3.receive()
    println("Coroutine1 received: $received")
}

suspend fun coroutine2(ch1: Channel<Int>, ch2: Channel<Int>, ch4: Channel<Int>) {
    val received1 = ch1.receive()
    val received2 = ch2.receive()
    println("Coroutine2 received: $received1 and $received2")
    ch4.send(3)
}

suspend fun deadlockCoroutine(ch3: Channel<Int>, ch4: Channel<Int>) {
    ch3.send(4)
    val received = ch4.receive()
    println("DeadlockCoroutine received: $received")
}

class RunChecker707: RunCheckerBase() {
    override fun block() = main()
}