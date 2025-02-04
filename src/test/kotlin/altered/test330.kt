/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test330
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA: Channel<Int>, val channelB: Channel<Int>)

class ClassB(val channelC: Channel<Int>, val channelD: Channel<Int>)

class ClassC(val channelD: Channel<Int>, val channelE: Channel<Int>)

class ClassD(val channelE: Channel<Int>, val channelA: Channel<Int>)

fun func1(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            val receivedA = classA.channelA.receive()
            classA.channelB.send(receivedA + 1)
            val receivedC = classB.channelC.receive()
            classB.channelD.send(receivedC - 1)
        }
    }
}

fun func2(classC: ClassC, classD: ClassD) {
    runBlocking {
        val job = launch {
            val receivedD = classC.channelD.receive()
            classC.channelE.send(receivedD + 1)
            val receivedE = classD.channelE.receive()
            classD.channelA.send(receivedE - 1)
        }
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelC, channelD)
    val classC = ClassC(channelD, channelE)
    val classD = ClassD(channelE, channelA)

    func1(classA, classB)
    func2(classC, classD)
}

class RunChecker330: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}