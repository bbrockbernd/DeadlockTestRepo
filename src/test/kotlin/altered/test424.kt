/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
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
package org.example.altered.test424
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA {
    val channelA = Channel<Int>()
    fun sendToA(value: Int) {
        runBlocking {
            channelA.send(value)
        }
    }

    suspend fun receiveFromA() {
        println("ClassA received: ${channelA.receive()}")
    }
}

class ClassB {
    val channelB = Channel<Int>()
    fun sendToB(value: Int) {
        runBlocking {
            channelB.send(value)
        }
    }

    suspend fun receiveFromB() {
        println("ClassB received: ${channelB.receive()}")
    }
}

class ClassC {
    val channelC = Channel<Int>()
    fun sendToC(value: Int) {
        runBlocking {
            channelC.send(value)
        }
    }

    suspend fun receiveFromC() {
        println("ClassC received: ${channelC.receive()}")
    }
}

fun function1(channelD: Channel<Int>) {
    runBlocking {
        channelD.send(10)
    }
}

suspend fun function2(channelD: Channel<Int>, channelE: Channel<Int>) {
    coroutineScope {
        launch {
            val value = channelD.receive()
            channelE.send(value)
        }
    }
}

suspend fun function3(channelE: Channel<Int>, channelF: Channel<Int>) {
    coroutineScope {
        launch {
            val value = channelE.receive()
            channelF.send(value)
        }
    }
}

fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()

    runBlocking {
        launch {
            classA.sendToA(1)

            classB.receiveFromB()

            function1(channelD)

            function2(channelD, channelE)

            function3(channelE, channelF)

            classC.receiveFromC()
          
            classB.sendToB(channelF.receive())

            classC.sendToC(classA.channelA.receive())
        }
    }
}

class RunChecker424: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}