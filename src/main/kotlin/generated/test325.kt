/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.generated.test325
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>(Channel.BUFFERED)
    suspend fun sendToA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<Int>()
    suspend fun sendToB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromB(): Int {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Int>(Channel.BUFFERED)
    val channelD = Channel<Int>()
    suspend fun sendToC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromC(): Int {
        return channelC.receive()
    }

    suspend fun sendToD(value: Int) {
        channelD.send(value)
    }

    suspend fun receiveFromD(): Int {
        return channelD.receive()
    }
}

suspend fun processChannelA(classA: ClassA, classC: ClassC) {
    classC.sendToC(classA.receiveFromA() + 1)
}

suspend fun processChannelB(classB: ClassB, classC: ClassC) {
    classC.sendToD(classB.receiveFromB() + 2)
}

fun main(): Unit= runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    launch {
        repeat(5) {
            classA.sendToA(it)
            delay(100)
        }
    }
    launch {
        repeat(5) {
            classB.sendToB(it)
            delay(200)
        }
    }

    coroutineScope {
        launch {
            repeat(5) {
                processChannelA(classA, classC)
            }
        }
        launch {
            repeat(5) {
                processChannelB(classB, classC)
            }
        }
        launch {
            repeat(5) {
                println("ChannelC: ${classC.receiveFromC()}")
                println("ChannelD: ${classC.receiveFromD()}")
            }
        }
    }
}