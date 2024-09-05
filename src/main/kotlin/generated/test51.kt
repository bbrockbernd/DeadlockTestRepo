/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.generated.test51
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<String>)
class ClassC(val channel: Channel<Double>)
class ClassD(val channel: Channel<Boolean>)
class ClassE(val channel: Channel<Int>)

val channel1 = Channel<Int>()
val channel2 = Channel<String>()
val channel3 = Channel<Double>()

fun function1(classA: ClassA, classB: ClassB) {
    runBlocking {
        launch {
            classA.channel.send(1)
        }
        launch {
            classB.channel.send("test")
        }
    }
}

fun function2(classC: ClassC) {
    runBlocking {
        launch {
            classC.channel.send(2.0)
        }
    }
}

fun function3(classD: ClassD) {
    runBlocking {
        launch {
            classD.channel.send(true)
        }
    }
}

fun function4(classA: ClassA) {
    runBlocking {
        launch {
            val received = classA.channel.receive()
        }
    }
}

fun function5(classE: ClassE) {
    runBlocking {
        launch {
            val received = classE.channel.receive()
        }
    }
}

val classA = ClassA(channel1)
val classB = ClassB(channel2)
val classC = ClassC(channel3)
val classD = ClassD(Channel()) // Additional channel to prevent deadlock
val classE = ClassE(channel1)

fun main(): Unit{
    function1(classA, classB)
    function2(classC)
    function3(classD)
    function4(classA)
    function5(classE)
}