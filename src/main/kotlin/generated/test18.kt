/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 6 different coroutines
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
package org.example.generated.test18
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel5: Channel<Int>, val channel6: Channel<Int>)
class ClassD(val channel7: Channel<Int>)
class ClassE(val channel8: Channel<Int>)

fun function1(a: ClassA, b: ClassB) {
    runBlocking {
        launch {
            val value1 = a.channel1.receive()
            a.channel2.send(value1)
        }
        launch {
            val value2 = b.channel3.receive()
            b.channel4.send(value2)
        }
    }
}

fun function2(c: ClassC, d: ClassD) {
    runBlocking {
        launch {
            val value3 = c.channel5.receive()
            c.channel6.send(value3)
        }
        launch {
            val value4 = d.channel7.receive()
            d.channel7.send(value4)  // Intentional deadlock
        }
    }
}

fun function3(e: ClassE, a: ClassA) {
    runBlocking {
        launch {
            val value5 = e.channel8.receive()
            a.channel1.send(value5)
        }
        launch {
            val value6 = a.channel2.receive()
            e.channel8.send(value6)
        }
    }
}

fun function4() = runBlocking {
    val channel = Channel<Int>()
    launch {
        channel.send(1)
        channel.receive()
    }
    launch {
        channel.send(2)
        channel.receive()
    }
}

fun function5(a: ClassA, d: ClassD) {
    runBlocking {
        launch {
            val value7 = a.channel1.receive()
            d.channel7.send(value7)
        }
        launch {
            val value8 = d.channel7.receive()
            a.channel1.send(value8)
        }
    }
}

fun function6(b: ClassB, c: ClassC) {
    runBlocking {
        launch {
            val value9 = b.channel3.receive()
            c.channel5.send(value9)
        }
        launch {
            val value10 = c.channel6.receive()
            b.channel4.send(value10)
        }
    }
}

fun function7(a: ClassA, c: ClassC) {
    runBlocking {
        launch {
            val value11 = a.channel1.receive()
            c.channel6.send(value11)
        }
        launch {
            val value12 = c.channel5.receive()
            a.channel2.send(value12)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel5, channel6)
    val classD = ClassD(channel7)
    val classE = ClassE(channel8)

    function1(classA, classB)
    function2(classC, classD)
    function3(classE, classA)
    function4()
    function5(classA, classD)
    function6(classB, classC)
    function7(classA, classC)
}