/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":7,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
- 2 different coroutines
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
package org.example.altered.test286
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>, val channel3: Channel<Int>)
class ClassB(val channel4: Channel<Int>, val channel5: Channel<Int>)
class ClassC(val channel6: Channel<Int>, val channel7: Channel<Int>)
class ClassD()

fun functionOne(classA: ClassA, classB: ClassB) = runBlocking {
    launch {
        classA.channel1.send(1)
        val received1 = classA.channel2.receive()
        classA.channel3.send(received1)

        val received4 = classB.channel4.receive()
        classB.channel5.send(2)
        classA.channel2.send(received4)
    }
}

fun functionTwo(classC: ClassC, classD: ClassD) = runBlocking {
    launch {
        val received6 = classC.channel6.receive()
        classC.channel7.send(received6 + 1)

        classC.channel6.send(3)
        val received7 = classC.channel7.receive()
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val classA = ClassA(channel1, channel2, channel3)
    val classB = ClassB(channel4, channel5)
    val classC = ClassC(channel6, channel7)
    val classD = ClassD()

    functionOne(classA, classB)
    functionTwo(classC, classD)
}

class RunChecker286: RunCheckerBase() {
    override fun block() = main()
}