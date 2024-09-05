/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 8 different coroutines
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
package org.example.generated.test254
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>)
class ClassB(val ch3: Channel<Int>, val ch4: Channel<Int>)
class ClassC(val ch5: Channel<Int>)
class ClassD(val ch6: Channel<Int>)
class ClassE(val ch7: Channel<Int>)

fun function1(a: ClassA, b: ClassB) = runBlocking {
    launch {
        val msg1 = a.ch1.receive()
        b.ch3.send(msg1)
    }

    launch {
        val msg2 = b.ch4.receive()
        a.ch2.send(msg2)
    }
}

fun function2(c: ClassC, d: ClassD) = runBlocking {
    launch {
        val msg3 = c.ch5.receive()
        d.ch6.send(msg3)
    }

    launch {
        val msg4 = d.ch6.receive()
        c.ch5.send(msg4)
    }
}

fun function3(a: ClassA, e: ClassE) = runBlocking {
    launch {
        val msg5 = a.ch2.receive()
        e.ch7.send(msg5)
    }

    launch {
        val msg6 = e.ch7.receive()
        a.ch1.send(msg6)
    }
}

fun function4(b: ClassB, e: ClassE) = runBlocking {
    launch {
        val msg7 = b.ch3.receive()
        e.ch7.send(msg7)
    }

    launch {
        val msg8 = e.ch7.receive()
        b.ch4.send(msg8)
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val a = ClassA(ch1, ch2)
    val b = ClassB(ch3, ch4)
    val c = ClassC(ch5)
    val d = ClassD(ch3)
    val e = ClassE(ch4)

    runBlocking {
        launch { function1(a, b) }
        launch { function2(c, d) }
        launch { function3(a, e) }
        launch { function4(b, e) }
    }
}