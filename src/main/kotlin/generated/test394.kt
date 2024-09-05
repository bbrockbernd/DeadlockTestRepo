/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":8,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
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
package org.example.generated.test394
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class A(val channelA: Channel<Int>, val channelB: Channel<String>)
class B(val channelC: Channel<Double>, val channelD: Channel<Long>)
class C(val channelE: Channel<Boolean>, val channelF: Channel<Char>, val channelG: Channel<Float>)

fun function1(channelA: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }
}

fun function2(channelB: Channel<String>) {
    GlobalScope.launch {
        channelB.send("Hello")
        channelB.send("World")
        channelB.close()
    }
}

fun function3(channelC: Channel<Double>, channelD: Channel<Long>) {
    GlobalScope.launch {
        for (i in channelC) {
            channelD.send(i.toLong())
        }
        channelD.close()
    }
}

fun function4(channelE: Channel<Boolean>, channelF: Channel<Char>) {
    GlobalScope.launch {
        while (!channelE.isClosedForReceive) {
            channelE.receiveOrNull()?.let {
                channelF.send(if (it) 'T' else 'F')
            }
        }
        channelF.close()
    }
}

fun function5(channelA: Channel<Int>, channelE: Channel<Boolean>) {
    GlobalScope.launch {
        for (i in channelA) {
            channelE.send(i % 2 == 0)
        }
        channelE.close()
    }
}

fun function6(channelG: Channel<Float>) {
    GlobalScope.launch {
        channelG.send(1.1f)
        channelG.send(2.2f)
        channelG.send(3.3f)
        channelG.close()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()
    val channelD = Channel<Long>()
    val channelE = Channel<Boolean>()
    val channelF = Channel<Char>()
    val channelG = Channel<Float>()

    val a = A(channelA, channelB)
    val b = B(channelC, channelD)
    val c = C(channelE, channelF, channelG)

    function1(a.channelA)
    function2(a.channelB)
    function3(b.channelC, b.channelD)
    function4(c.channelE, c.channelF)
    function5(a.channelA, c.channelE)
    function6(c.channelG)

    delay(1000)
}