/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.generated.test758
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
}

class ClassB {
    val channelB = Channel<Int>()
}

class ClassC {
    val channelC = Channel<Int>()
}

fun function1(scope: CoroutineScope, classA: ClassA, classB: ClassB) {
    scope.launch {
        val value = classA.channelA.receive()
        classB.channelB.send(value)
    }
}

fun function2(scope: CoroutineScope, classB: ClassB, classC: ClassC) {
    scope.launch {
        val value = classB.channelB.receive()
        classC.channelC.send(value)
    }
}

fun function3(scope: CoroutineScope, classC: ClassC, classA: ClassA) {
    scope.launch {
        val value = classC.channelC.receive()
        classA.channelA.send(value)
    }
}

fun function4(classA: ClassA) {
    runBlocking {
        classA.channelA.send(1)
    }
}

fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    runBlocking {
        function1(this, classA, classB)
        function2(this, classB, classC)
        function3(this, classC, classA)
        function4(classA)
    }
}