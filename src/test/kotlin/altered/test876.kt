/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test876
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    fun sendToChannel1(value: Int) = runBlocking { channel1.send(value) }
}

class ClassB {
    val channel2 = Channel<Int>()
    fun sendToChannel2(value: Int) = runBlocking { channel2.send(value) }
}

class ClassC {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun sendToChannel3(value: Int) = runBlocking { channel3.send(value) }
    fun sendToChannel4(value: Int) = runBlocking { channel4.send(value) }
}

fun function1(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            val received = classB.channel2.receive()
            classA.sendToChannel1(received)
        }
        launch {
            val received = classA.channel1.receive()
            classB.sendToChannel2(received)
        }
    }
}

fun function2(classC: ClassC, classA: ClassA) {
    runBlocking {
        launch {
            val received = classC.channel3.receive()
            classA.sendToChannel1(received)
        }
        launch {
            val received = classA.channel1.receive()
            classC.sendToChannel3(received)
        }
    }
}

fun function3(classC: ClassC) {
    runBlocking {
        launch {
            val received = classC.channel4.receive()
            classC.sendToChannel3(received)
        }
        launch {
            val received = classC.channel3.receive()
            classC.sendToChannel4(received)
        }
    }
}

fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    runBlocking {
        launch { function1(classA, classB) }
        launch { function2(classC, classA) }
        launch { function3(classC) }

        launch {
            classB.sendToChannel2(42)
            classC.sendToChannel4(24)
        }
    }
}

class RunChecker876: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}