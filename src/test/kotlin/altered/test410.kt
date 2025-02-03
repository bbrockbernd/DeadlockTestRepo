/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test410
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)
class ClassD(val channel: Channel<Int>)
class ClassE(val channel: Channel<Int>)

fun function1(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            val value = classA.channel.receive()
            classB.channel.send(value + 1)
        }
    }
}

fun function2(classB: ClassB, classC: ClassC) {
    runBlocking {
        launch {
            val value = classB.channel.receive()
            classC.channel.send(value + 1)
        }
    }
}

fun function3(classC: ClassC, classD: ClassD) {
    runBlocking {
        launch {
            val value = classC.channel.receive()
            classD.channel.send(value + 1)
        }
    }
}

fun function4(classE: ClassE, classA: ClassA) {
    runBlocking {
        launch {
            val value = classE.channel.receive()
            classA.channel.send(value + 1)
        }
    }
}

fun main(): Unit {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    
    val classA = ClassA(channelA)
    val classB = ClassB(channelB)
    val classC = ClassC(channelC)
    val classD = ClassD(channelD)
    val classE = ClassE(channelA) // This class uses the same channel as classA to cause potential deadlock
    
    coroutineScope {
        launch { function1(classA, classB) }
        launch { function2(classB, classC) }
        launch { function3(classC, classD) }
        launch { function4(classE, classA) }
        launch {
            classA.channel.send(1) // Initial trigger to start the chain
        }
        launch {
            classE.channel.send(2) // Second trigger that goes back to classA
        }
        launch { 
            while (true) delay(1000) // Idle coroutine to keep coroutineScope active
        }
    }
}

class RunChecker410: RunCheckerBase() {
    override fun block() = main()
}