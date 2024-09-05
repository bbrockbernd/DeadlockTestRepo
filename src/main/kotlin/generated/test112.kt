/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.generated.test112
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class ClassB {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
}

class ClassC {
    val channel5 = Channel<Int>()
}

// Function 1
fun function1(classA: ClassA) {
    runBlocking {
        launch {
            classA.channel1.receive()
            classA.channel2.send(1)
        }
    }
}

// Function 2
fun function2(classA: ClassA) {
    runBlocking {
        launch {
            classA.channel2.receive()
            classA.channel1.send(1)
        }
    }
}

// Function 3
fun function3(classB: ClassB) {
    runBlocking {
        launch {
            classB.channel3.receive()
            classB.channel4.send(1)
        }
    }
}

// Function 4
fun function4(classB: ClassB) {
    runBlocking {
        launch {
            classB.channel4.receive()
            classB.channel3.send(1)
        }
    }
}

// Function 5
fun function5(classC: ClassC, classA: ClassA) {
    runBlocking {
        launch {
            classA.channel1.send(1)
            classC.channel5.receive()
        }
    }
}

// Function 6
fun function6(classB: ClassB, classC: ClassC) {
    runBlocking {
        launch {
            classB.channel3.send(1)
            classC.channel5.send(1)
        }
    }
}

// Function 7
fun function7(classC: ClassC, classB: ClassB) {
    runBlocking {
        launch {
            classC.channel5.receive()
            classB.channel3.send(1)
        }
    }
}

// Function 8
fun function8(classA: ClassA, classC: ClassC) {
    runBlocking {
        launch {
            classA.channel2.send(1)
            classC.channel5.receive()
        }
    }
}

// Initialize and execute
fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    function1(classA)
    function2(classA)
    function3(classB)
    function4(classB)
    function5(classC, classA)
    function6(classB, classC)
    function7(classC, classB)
    function8(classA, classC)
}