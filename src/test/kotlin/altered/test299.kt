/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.altered.test299
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<Int>)
class C(val channel: Channel<Int>)
class D(val channel: Channel<Int>)
class E(val channel: Channel<Int>)

suspend fun function1(a: A, b: B) {
    a.channel.send(1)
    val received = b.channel.receive()
}

suspend fun function2(c: C, d: D) {
    c.channel.send(2)
    val received = d.channel.receive()
}

suspend fun function3(e: E, b: B) {
    e.channel.send(3)
    val received = b.channel.receive()
}

suspend fun function4(a: A, d: D) {
    d.channel.send(a.channel.receive())
}

suspend fun function5(c: C, e: E) {
    e.channel.send(c.channel.receive())
}

fun mainFunction() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val a = A(channel1)
    val b = B(channel2)
    val c = C(channel3)
    val d = D(channel4)
    val e = E(channel1)

    runBlocking {
        launch {
            function1(a, b)
            function2(c, d)
            function3(e, b)
            function4(a, d)
            function5(c, e)
        }
    }
}

fun main(): Unit{
    mainFunction()
}

class RunChecker299: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}