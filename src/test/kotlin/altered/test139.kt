/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.altered.test139
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch1: Channel<Int>, val ch2: Channel<String>) {
    suspend fun sendIntA(value: Int) {
        ch1.send(value)
    }

    suspend fun sendStringA(value: String) {
        ch2.send(value)
    }
}

class B(val ch3: Channel<Double>) {
    suspend fun sendDoubleB(value: Double) {
        ch3.send(value)
    }

    suspend fun receiveDoubleB(): Double {
        return ch3.receive()
    }
}

class C(val ch1: Channel<Int>, val ch2: Channel<String>, val ch3: Channel<Double>) {
    suspend fun receiveIntC(): Int {
        return ch1.receive()
    }

    suspend fun receiveStringC(): String {
        return ch2.receive()
    }

    suspend fun sendDoubleC(value: Double) {
        ch3.send(value)
    }
}

fun fun1(channel: Channel<Int>) = runBlocking {
    launch {
        repeat(5) { channel.send(it) }
        channel.close()
    }
}

fun fun2(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in channel) println("Received $x")
    }
}

fun fun3(channel: Channel<Double>) = runBlocking {
    launch {
        repeat(5) { channel.send(it * 1.1) }
        channel.close()
    }
}

fun fun4(channel: Channel<Double>) = runBlocking {
    launch {
        for (x in channel) println("Received $x")
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<String>()
    val ch3 = Channel<Double>()
    val a = A(ch1, ch2)
    val b = B(ch3)
    val c = C(ch1, ch2, ch3)

    val job1 = launch { a.sendIntA(10) }
    val job2 = launch { a.sendStringA("Hello") }
    val job3 = launch { c.receiveIntC() }
    val job4 = launch { c.receiveStringC() }
    val job5 = launch { c.sendDoubleC(2.5) }
    val job6 = launch { b.receiveDoubleB() }
    val job7 = launch { b.sendDoubleB(3.14) }

    job1.join()
    job2.join()
    job3.join()
    job4.join()
    job5.join()
    job6.join()
    job7.join()
    
    fun1(ch1)
    fun2(ch1)
    fun3(ch3)
    fun4(ch3)
}

class RunChecker139: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}