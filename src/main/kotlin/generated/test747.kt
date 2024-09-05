/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.generated.test747
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    suspend fun sendA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<Int>()
    suspend fun sendB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveB(): Int {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Int>()
    suspend fun sendC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveC(): Int {
        return channelC.receive()
    }
}

fun function1(classA: ClassA, value: Int) = runBlocking {
    launch {
        classA.sendA(value)
    }
}

fun function2(classB: ClassB, value: Int) = runBlocking {
    launch {
        classB.sendB(value)
    }
}

fun function3(classC: ClassC, value: Int) = runBlocking {
    launch {
        classC.sendC(value)
    }
}

fun function4(classA: ClassA, classB: ClassB, classC: ClassC) = runBlocking {
    launch {
        val resultA = classA.receiveA()
        classB.sendB(resultA)
        val resultB = classB.receiveB()
        classC.sendC(resultB)
    }
}

val channelD = Channel<Int>()
val channelE = Channel<Int>()

fun main(): Unit= runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    val job1 = launch {
        function1(classA, 1)
    }

    val job2 = launch {
        function2(classB, 2)
    }

    val job3 = launch {
        function4(classA, classB, classC)
    }

    val resultD = classA.receiveA()
    channelD.send(resultD)

    val resultE = classC.receiveC()
    channelE.send(resultE)
    
    job1.join()
    job2.join()
    job3.join()
}