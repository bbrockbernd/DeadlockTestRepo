/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 4 different coroutines
- 4 different classes

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
package org.example.altered.test329
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
}

class ClassB {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(5)
}

class ClassC {
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>(5)
}

class ClassD {
    suspend fun function1(classA: ClassA) {
        for (i in 1..10) {
            classA.channel1.send(i)
            println("ClassD sent: $i to channel1")
        }
        repeat(10) {
            val value = classA.channel2.receive()
            println("ClassD received: $value from channel2")
        }
    }
    
    suspend fun function2(classB: ClassB) {
        for (i in 1..10) {
            classB.channel3.send(i)
            println("ClassD sent: $i to channel3")
        }
        repeat(10) {
            val value = classB.channel4.receive()
            println("ClassD received: $value from channel4")
        }
    }
    
    suspend fun function3(classC: ClassC) {
        for (i in 1..10) {
            classC.channel5.send(i)
            println("ClassD sent: $i to channel5")
        }
        repeat(10) {
            val value = classC.channel6.receive()
            println("ClassD received: $value from channel6")
        }
    }
}

fun main(): Unit= runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    val classD = ClassD()

    launch {
        classD.function1(classA)
    }
    launch {
        for (i in 1..10) {
            val value = classA.channel1.receive()
            println("Main received: $value from channel1")
            classA.channel2.send(value * 10)
            println("Main sent: ${value * 10} to channel2")
        }
    }

    launch {
        classD.function2(classB)
    }
    launch {
        for (i in 1..10) {
            val value = classB.channel3.receive()
            println("Main received: $value from channel3")
            classB.channel4.send(value * 10)
            println("Main sent: ${value * 10} to channel4")
        }
    }

    launch {
        classD.function3(classC)
    }
    launch {
        for (i in 1..10) {
            val value = classC.channel5.receive()
            println("Main received: $value from channel5")
            classC.channel6.send(value * 10)
            println("Main sent: ${value * 10} to channel6")
        }
    }
}

class RunChecker329: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}