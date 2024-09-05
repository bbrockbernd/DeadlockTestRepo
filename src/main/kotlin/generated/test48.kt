/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":8,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
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
package org.example.generated.test48
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class B {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
}

class C {
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
}

fun function1(a: A) = runBlocking {
    launch {
        a.channel1.send(1)
        a.channel2.receive()
    }
}

fun function2(a: A) = runBlocking {
    launch {
        a.channel2.send(2)
        a.channel1.receive()
    }
}

fun function3(b: B) = runBlocking {
    launch {
        b.channel3.send(3)
        b.channel4.receive()
    }
}

fun function4(b: B) = runBlocking {
    launch {
        b.channel4.send(4)
        b.channel3.receive()
    }
}

fun function5(c: C) = runBlocking {
    launch {
        c.channel5.send(5)
        c.channel6.receive()
    }
}

fun function6(c: C) = runBlocking {
    launch {
        c.channel6.send(6)
        c.channel5.receive()
    }
}

fun function7(a: A, b: B) = runBlocking {
    launch {
        a.channel1.receive()
        b.channel3.send(7)
    }
}

fun function8(a: A, c: C) = runBlocking {
    launch {
        a.channel2.receive()
        c.channel6.send(8)
    }
}

fun main(): Unit{
    val a = A()
    val b = B()
    val c = C()

    function1(a)
    function2(a)
    function3(b)
    function4(b)
    function5(c)
    function6(c)
    function7(a, b)
    function8(a, c)
}