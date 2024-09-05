/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.generated.test171
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<String>)
class ClassB(val channel1: Channel<Int>)
class ClassC(val channel2: Channel<String>)
class ClassD(val channel1: Channel<Int>, val channel2: Channel<String>)
class ClassE(val channel1: Channel<Int>, val channel2: Channel<String>)

fun function1(classA: ClassA) {
    GlobalScope.launch {
        classA.channel1.send(1)
        classA.channel2.send("Hello")
    }
}

suspend fun function2(classB: ClassB) {
    coroutineScope {
        launch {
            val value = classB.channel1.receive()
            println("Received in function2: $value")
        }
    }
}

fun function3(classC: ClassC) {
    GlobalScope.launch {
        val message = classC.channel2.receive()
        println("Received in function3: $message")
    }
}

suspend fun function4(classD: ClassD, classE: ClassE) {
    coroutineScope {
        launch {
            val value = classD.channel1.receive()
            val message = classD.channel2.receive()
            println("Received in function4 from classD: $value, $message")
        }
        launch {
            classE.channel1.send(2)
            classE.channel2.send("World")
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel1)
    val classC = ClassC(channel2)
    val classD = ClassD(channel1, channel2)
    val classE = ClassE(channel1, channel2)

    runBlocking {
        function1(classA)
        function2(classB)
        function3(classC)
        function4(classD, classE)
    }
}