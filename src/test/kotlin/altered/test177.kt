/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":7,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
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
package org.example.altered.test177
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>)
class ClassC(val channel4: Channel<Int>)
class ClassD(val a: ClassA)
class ClassE(val b: ClassB, val c: ClassC)

fun function1(a: ClassA): Int {
    runBlocking {
        launch {
            a.channel1.send(1)
        }
        launch {
            a.channel2.receive()
        }
    }
    return 1
}

fun function2(b: ClassB) {
    runBlocking {
        launch {
            b.channel3.receive()
        }
    }
}

fun function3(c: ClassC) {
    runBlocking {
        launch {
            c.channel4.send(2)
        }
    }
}

fun function4(d: ClassD) {
    runBlocking {
        launch {
            d.a.channel1.receive()
        }
    }
}

fun function5(e: ClassE) {
    runBlocking {
        launch {
            e.b.channel3.receive()
            e.c.channel4.send(3)
        }
    }
}

fun function6(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(4)
            channel2.receive()
        }
    }
}

fun function7(channel4: Channel<Int>) {
    runBlocking {
        launch {
            channel4.receive()
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3)
    val classC = ClassC(channel4)
    val classD = ClassD(classA)
    val classE = ClassE(classB, classC)

    runBlocking {
        launch {
            function1(classA)
        }
        launch {
            function2(classB)
        }
        launch {
            function3(classC)
        }
        launch {
            function4(classD)
        }
        launch {
            function5(classE)
        }
        launch {
            function6(channel1, channel2)
        }
        launch {
            function7(channel4)
        }
    }
}

class RunChecker177: RunCheckerBase() {
    override fun block() = main()
}