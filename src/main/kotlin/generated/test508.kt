/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.generated.test508
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()

    fun sendToChannelA() {
        runBlocking {
            launch {
                channelA.send(1)
            }
        }
    }
}

class ClassB {
    val channelB = Channel<Int>()

    fun receiveFromChannelA(channelA: Channel<Int>) {
        runBlocking {
            launch {
                val received = channelA.receive()
                channelB.send(received)
            }
        }
    }
}

class ClassC {
    val channelB: Channel<Int> = Channel()

    fun receiveFromChannelB(channelB: Channel<Int>) {
        runBlocking {
            launch {
                val received = channelB.receive()
                println("Received from channelB: $received")
            }
        }
    }
}

fun functionOne(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            classA.sendToChannelA()
            classB.receiveFromChannelA(classA.channelA)
        }
    }
}

fun functionTwo(classB: ClassB, classC: ClassC) {
    runBlocking {
        launch {
            classC.receiveFromChannelB(classB.channelB)
        }
    }
}

fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    functionOne(classA, classB)
    functionTwo(classB, classC)

    runBlocking {
        launch {
            classC.channelB.send(2) // Simulate sending some value to unblock receive
        }
    }
}