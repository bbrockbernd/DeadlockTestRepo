/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.altered.test365
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class A {
    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()

    suspend fun sendToChannelA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromChannelA(): Int {
        return channelA.receive()
    }

    suspend fun sendToChannelB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromChannelB(): Int {
        return channelB.receive()
    }
}

class B(private val a: A) {
    suspend fun processAtoB() {
        val value = a.receiveFromChannelA()
        a.sendToChannelB(value * 2)
    }
}

class C(private val a: A) {
    suspend fun processBtoA() {
        val value = a.receiveFromChannelB()
        a.sendToChannelA(value / 2)
    }
}

suspend fun function1(a: A) {
    for (i in 1..5) {
        a.sendToChannelA(i)
    }
}

suspend fun function2(a: A, b: B) {
    for (i in 1..5) {
        b.processAtoB()
    }
}

suspend fun function3(a: A, c: C) {
    for (i in 1..5) {
        c.processBtoA()
    }
}

fun main(): Unit= runBlocking {
    val a = A()
    val b = B(a)
    val c = C(a)

    launch { function1(a) }
    launch { function1(a) }
    launch { function2(a, b) }
    launch { function2(a, b) }
    launch { function3(a, c) }
    launch { function3(a, c) }
    launch { function1(a) }
    launch { function2(a, b) }

    delay(1000L) // Just to keep the application alive for a little while
}

class RunChecker365: RunCheckerBase() {
    override fun block() = main()
}