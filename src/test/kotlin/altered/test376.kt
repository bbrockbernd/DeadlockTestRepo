/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 8 different coroutines
- 5 different classes

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
package org.example.altered.test376
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)
class ClassD(val channel: Channel<Int>)
class ClassE(val channel: Channel<Int>)

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking {
        launch {
            channelA.send(1)
            channelB.receive()
        }
        launch {
            channelB.send(2)
            channelA.receive()
        }
    }
}

fun function2(channelC: Channel<Int>, channelD: Channel<Int>) {
    runBlocking {
        launch {
            channelC.send(3)
            channelD.receive()
        }
        launch {
            channelD.send(4)
            channelC.receive()
        }
    }
}

fun function3(channelE: Channel<Int>, channelA: Channel<Int>) {
    runBlocking {
        launch {
            channelE.send(5)
            channelA.receive()
        }
        launch {
            channelA.send(6)
            channelE.receive()
        }
    }
}

fun function4(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking {
        launch {
            channelB.send(7)
            channelC.receive()
        }
        launch {
            channelC.send(8)
            channelB.receive()
        }
    }
}

fun main(): Unit{
    val classA = ClassA(Channel())
    val classB = ClassB(Channel())
    val classC = ClassC(Channel())
    val classD = ClassD(Channel())
    val classE = ClassE(Channel())

    function1(classA.channel, classB.channel)
    function2(classC.channel, classD.channel)
    function3(classE.channel, classA.channel)
    function4(classB.channel, classC.channel)
}

class RunChecker376: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}