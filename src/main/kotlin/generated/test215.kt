/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 3 different coroutines
- 5 different classes

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
package org.example.generated.test215
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)

class B(val channel3: Channel<Int>)

class C(val channel4: Channel<Int>)

class D(val channel5: Channel<Int>)

class E(val channel6: Channel<Int>)

suspend fun func1(a: A) {
    a.channel1.send(1)
    a.channel2.send(a.channel1.receive())
}

suspend fun func2(b: B, d: D) {
    b.channel3.send(2)
    d.channel5.send(b.channel3.receive())
}

suspend fun func3(c: C, e: E) {
    e.channel6.send(3)
    c.channel4.send(e.channel6.receive())
}

suspend fun func4(a: A, b: B) {
    a.channel2.send(b.channel3.receive())
}

suspend fun func5(c: C, d: D) {
    c.channel4.send(d.channel5.receive())
}

suspend fun func6(e: E) {
    e.channel6.send(6)
    e.channel6.receive()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel3)
    val c = C(channel4)
    val d = D(channel5)
    val e = E(channel6)

    launch { func1(a) }
    launch { func2(b, d) }
    launch { func3(c, e) }

    func4(a, b)
    func5(c, d)
    func6(e)
}