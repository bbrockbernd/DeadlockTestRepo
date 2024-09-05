/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":6,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 6 different coroutines
- 4 different classes

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
package org.example.generated.test175
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<String>)
class C(val channel: Channel<Boolean>)
class D(val channel: Channel<Double>)
class E(val channel: Channel<Long>)

fun function1(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

suspend fun function2(channel: Channel<String>) {
    coroutineScope {
        launch {
            listOf("A", "B", "C", "D", "E").forEach {
                channel.send(it)
            }
        }
    }
}

fun function3(channel: Channel<Boolean>) {
    GlobalScope.launch {
        for (b in listOf(true, false, true)) {
            channel.send(b)
        }
    }
}

fun function4(channel: Channel<Double>) {
    GlobalScope.launch {
        for (d in listOf(1.1, 2.2, 3.3)) {
            channel.send(d)
        }
    }
}

fun function5(channel: Channel<Long>) {
    GlobalScope.launch {
        for (l in listOf(10L, 20L, 30L)) {
            channel.send(l)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Boolean>()
    val channel4 = Channel<Double>()
    val channel5 = Channel<Long>()

    val a = A(channel1)
    val b = B(channel2)
    val c = C(channel3)
    val d = D(channel4)
    val e = E(channel5)

    function1(a.channel)
    function2(b.channel)
    function3(c.channel)
    function4(d.channel)
    function5(e.channel)

    repeat(5) {
        println(a.channel.receive())
        println(b.channel.receive())
    }

    repeat(3) {
        println(c.channel.receive())
        println(d.channel.receive())
        println(e.channel.receive())
    }
}