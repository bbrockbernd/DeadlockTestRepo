/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.generated.test702
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

fun function3(classA: ClassA, classC: ClassC) {
    runBlocking {
        launch {
            classC.channelC.send(3)
            classA.channelA.receive()
        }
    }
}

val classA = ClassA()
val classB = ClassB()
val classC = ClassC()

runBlocking {
    launch {
        function1(classA, classB)
    }
    launch {
        function2(classB, classC)
    }
    launch {
        function3(classA, classC)
    }

    launch {
        classA.channelA.receive()
    }

    launch {
        classC.channelC.receive()
    }
}