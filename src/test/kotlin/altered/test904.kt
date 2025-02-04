/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test904
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveFromChannel2(): Int {
        return channel2.receive()
    }
}

class ClassB {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    suspend fun sendToChannel3(value: Int) {
        channel3.send(value)
    }

    suspend fun receiveFromChannel4(): Int {
        return channel4.receive()
    }
}

fun configChannelA(classA: ClassA) = runBlocking {
    launch {
        classA.sendToChannel1(1)
        classA.receiveFromChannel2()
    }
}

fun configChannelB(classB: ClassB) = runBlocking {
    launch {
        classB.sendToChannel3(2)
        classB.receiveFromChannel4()
    }
}

fun initiateDeadlock(classA: ClassA, classB: ClassB) = runBlocking {
    val job1 = launch {
        classA.sendToChannel1(classB.receiveFromChannel4())
    }

    val job2 = launch {
        classB.sendToChannel3(classA.receiveFromChannel2())
    }

    job1.join()
    job2.join()
}

fun main(): Unit= runBlocking {
    val classA = ClassA()
    val classB = ClassB()

    launch {
        configChannelA(classA)
    }

    launch {
        configChannelB(classB)
    }

    initiateDeadlock(classA, classB)
}

class RunChecker904: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}