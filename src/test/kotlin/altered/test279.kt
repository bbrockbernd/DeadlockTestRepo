/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
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
package org.example.altered.test279
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA: Channel<Int>)
class ClassB(val channelB: Channel<Int>)
class ClassC(val channelC: Channel<Int>)
class ClassD(val channelD: Channel<Int>)

fun function1(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            classA.channelA.send(1)
            classB.channelB.receive()
        }
    }
}

fun function2(classB: ClassB, classC: ClassC) {
    runBlocking {
        launch {
            classB.channelB.send(2)
            classC.channelC.receive()
        }
    }
}

fun function3(classC: ClassC, classD: ClassD) {
    runBlocking {
        launch {
            classC.channelC.send(3)
            classD.channelD.receive()
        }
    }
}

fun function4(classD: ClassD, classA: ClassA) {
    runBlocking {
        launch {
            classD.channelD.send(4)
            classA.channelA.receive()
        }
    }
}

fun function5(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(5)
            channel2.receive()
        }
    }
}

fun function6(channel3: Channel<Int>, channel4: Channel<Int>) {
    runBlocking {
        launch {
            channel3.send(6)
            channel4.receive()
        }
    }
}

fun function7(channel5: Channel<Int>, channel6: Channel<Int>) {
    runBlocking {
        launch {
            channel5.send(7)
            channel6.receive()
        }
    }
}

fun function8(channel7: Channel<Int>, channel8: Channel<Int>) {
    runBlocking {
        launch {
            channel7.send(8)
            channel8.receive()
        }
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()
val channel7 = Channel<Int>()
val channel8 = Channel<Int>()

val classA = ClassA(channel1)
val classB = ClassB(channel2)
val classC = ClassC(channel3)
val classD = ClassD(channel4)

fun main(): Unit{
    function1(classA, classB)
    function2(classB, classC)
    function3(classC, classD)
    function4(classD, classA)
    function5(channel5, channel6)
    function6(channel6, channel7)
    function7(channel7, channel8)
    function8(channel8, channel5)
}

class RunChecker279: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}