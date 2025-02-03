/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test35
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel: Channel<Int>) {
    suspend fun sendValue(value: Int) {
        channel.send(value)
    }

    suspend fun receiveValue(): Int {
        return channel.receive()
    }
}

class ClassB(private val classA: ClassA) {
    suspend fun transmitData(data: Int) {
        classA.sendValue(data)
    }

    suspend fun fetchData(): Int {
        return classA.receiveValue()
    }
}

class ClassC(private val classB: ClassB) {
    suspend fun initProcess(data: Int): Int {
        classB.transmitData(data)
        return classB.fetchData()
    }
}

fun processChannel(channel: Channel<Int>) {
    runBlocking {
        val classA = ClassA(channel)
        val classB = ClassB(classA)
        val classC = ClassC(classB)
        
        launch {
            println("Received Value: ${classC.initProcess(42)}")
        }

        println("Send Value: 42")
        classA.sendValue(42) // Sender will be blocked here
    }
}

suspend fun main(): Unit {
    val channel = Channel<Int>()
    processChannel(channel)
}

class RunChecker35: RunCheckerBase() {
    override fun block() = main()
}