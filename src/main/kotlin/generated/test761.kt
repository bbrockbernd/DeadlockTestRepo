/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
- lists, arrays or other datastructures
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
package org.example.generated.test761
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
}

class ClassB {
    val channelC = Channel<Int>()
}

class ClassC {
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
}

fun function1(a: ClassA) {
    runBlocking {
        launch {
            val value = a.channelA.receive()
            a.channelB.send(value)
        }
    }
}

fun function2(b: ClassB) {
    runBlocking {
        launch {
            val value = b.channelC.receive()
            b.channelC.send(value)
        }
    }
}

fun function3(c: ClassC, a: ClassA) {
    runBlocking {
        launch {
            val value1 = c.channelD.receive()
            val value2 = a.channelB.receive()
            c.channelE.send(value1 + value2)
        }
    }
}

fun function4(c: ClassC, b: ClassB) {
    runBlocking {
        launch {
            val value1 = c.channelE.receive()
            b.channelC.send(value1)
        }
    }
}

fun main(): Unit{
    val a = ClassA()
    val b = ClassB()
    val c = ClassC()

    function1(a)
    function2(b)
    function3(c, a)
    function4(c, b)

    runBlocking {
        launch {
            c.channelD.send(1)
            a.channelA.send(2)
        }
    }
}