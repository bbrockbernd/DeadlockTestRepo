/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 8 different channels
- 2 different coroutines
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
package org.example.generated.test476
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class A {
    val ch1 = Channel<Int>(Channel.UNLIMITED)
    val ch2 = Channel<Int>(Channel.UNLIMITED)
    fun sendToCh1(value: Int) = runBlocking {
        ch1.send(value)
    }
    fun receiveFromCh2() = runBlocking {
        ch2.receive()
    }
}

class B {
    val ch3 = Channel<Int>(Channel.UNLIMITED)
    val ch4 = Channel<String>(Channel.UNLIMITED)
    fun sendToCh3(value: Int) = runBlocking {
        ch3.send(value)
    }
    fun receiveFromCh4() = runBlocking {
        ch4.receive()
    }
}

class C {
    val ch5 = Channel<Int>(Channel.UNLIMITED)
    val ch6 = Channel<String>(Channel.UNLIMITED)
    val ch7 = Channel<Int>(Channel.UNLIMITED)
    val ch8 = Channel<String>(Channel.UNLIMITED)
    fun sendToCh5(value: Int) = runBlocking {
        ch5.send(value)
    }
    fun receiveFromCh6() = runBlocking {
        ch6.receive()
    }
}

suspend fun process1(a: A, b: B, c: C) = coroutineScope {
    launch {
        a.sendToCh1(1)
        val valueA = a.receiveFromCh2()
        b.sendToCh3(valueA)
    }
    launch {
        b.sendToCh3(2)
        val valueB = b.receiveFromCh4()
        c.sendToCh5(valueB)
    }
}

suspend fun process2(c: C, b: B) = coroutineScope {
    launch {
        c.sendToCh5(3)
        val valueC = c.receiveFromCh6()
        b.sendToCh3(valueC)
    }
    launch {
        c.sendToCh7(4)
        val valueC = c.ch8.receive()
        c.sendToCh5(valueC.toInt())
    }
}

fun main(): Unit = runBlocking {
    val a = A()
    val b = B()
    val c = C()

    launch {
        process1(a, b, c)
    }
    launch {
        process2(c, b)
    }
}