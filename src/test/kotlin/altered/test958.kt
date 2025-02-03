/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test958
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>) {
    suspend fun methodA() {
        val data = channel1.receive()
        println("ClassA received: $data")
    }
}

class ClassB(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun methodB() {
        val data = channel2.receive()
        channel1.send(data)
        println("ClassB received and sent: $data")
    }
}

class ClassC(val channel2: Channel<Int>) {
    suspend fun methodC() {
        val data = 42
        channel2.send(data)
        println("ClassC sent: $data")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel1, channel2)
    val classC = ClassC(channel2)

    launch {
        classA.methodA()
    }
    
    launch {
        classB.methodB()
    }
    
    launch {
        classC.methodC()
    }
}

class RunChecker958: RunCheckerBase() {
    override fun block() = main()
}