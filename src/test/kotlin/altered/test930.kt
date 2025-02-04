/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.altered.test930
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
}

fun functionOne(classA: ClassA, classB: ClassB) = runBlocking {
    launch {
        classA.channelA.send(1)
        classB.channelE.receive()
    }

    launch {
        classB.channelC.send(2)
        classA.channelB.receive()
    }
}

fun functionTwo(classA: ClassA, classB: ClassB) = runBlocking {
    launch {
        classB.channelD.send(3)
        classB.channelC.receive()
    }

    launch {
        classA.channelB.send(4)
        classB.channelD.receive()
    }
}

val classA = ClassA()
val classB = ClassB()

fun main(): Unit= runBlocking {
    functionOne(classA, classB)
    functionTwo(classA, classB)
}

class RunChecker930: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}