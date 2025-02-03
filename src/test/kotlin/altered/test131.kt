/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test131
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val chA: Channel<Int>)
class ClassB(val chB: Channel<Int>)
class ClassC(val chC: Channel<Int>)
class ClassD(val chD: Channel<Int>)
class ClassE(val chE: Channel<Int>)

fun function1(ch: Channel<Int>) {
    runBlocking {
        launch {
            ch.send(1)
        }
    }
}

fun function2(ch: Channel<Int>) {
    runBlocking {
        launch {
            ch.send(2)
        }
    }
}

fun function3(ch: Channel<Int>) {
    runBlocking {
        launch {
            println(ch.receive())
        }
    }
}

fun function4(ch: Channel<Int>) {
    runBlocking {
        launch {
            println(ch.receive())
        }
    }
}

fun function5(ch: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        launch {
            ch.send(3)
            ch2.receive()
        }
    }
}

fun function6(ch: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        launch {
            ch.receive()
            ch2.send(4)
        }
    }
}

fun function7(ch: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        launch {
            ch.send(5)
            ch2.receive()
        }
    }
}

fun function8(ch: Channel<Int>) {
    runBlocking {
        launch {
            ch.send(6)
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val a = ClassA(ch1)
    val b = ClassB(ch2)
    val c = ClassC(ch3)
    val d = ClassD(ch4)
    val e = ClassE(ch5)

    runBlocking {
        launch {
            function1(a.chA)
            function2(b.chB)
            function5(a.chA, b.chB)
        }

        launch {
            function3(c.chC)
            function4(d.chD)
            function6(c.chC, d.chD)
        }

        launch {
            function7(e.chE, ch6)
        }

        launch {
            function8(ch7)
        }

        launch {
            ch8.receive()
        }

        launch {
            ch8.send(0)
        }
    }
}

class RunChecker131: RunCheckerBase() {
    override fun block() = main()
}