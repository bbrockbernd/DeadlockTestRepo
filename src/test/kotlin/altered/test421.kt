/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 8 different coroutines
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
package org.example.altered.test421
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class ClassB {
    val channel3 = Channel<String>()
    val channel4 = Channel<Double>()
}

class ClassC {
    val channel5 = Channel<Long>()
    val channel6 = Channel<Float>()
}

fun functionA(classA: ClassA) = runBlocking {
    val channel7 = Channel<Int>()
    val channel8 = Channel<Unit>()

    launch {
        classA.channel1.send(1)
        classA.channel2.receive()
    }
    
    launch {
        channel7.send(classA.channel1.receive())
        classA.channel2.send(2)
    }
    
    launch {
        channel8.receive()
    }
    
    launch {
        channel7.close()
    }
}

fun functionB(classB: ClassB) = runBlocking {
    val channel7 = Channel<String>()
    val channel8 = Channel<Double>()

    launch {
        classB.channel3.send("test")
        classB.channel4.receive()
    }
    
    launch {
        channel7.send(classB.channel3.receive())
        classB.channel4.send(3.14)
    }
    
    launch {
        channel8.receive()
    }
    
    launch {
        channel7.close()
    }
}

fun functionC(classC: ClassC) = runBlocking {
    val channel7 = Channel<Long>()
    val channel8 = Channel<Float>()

    launch {
        classC.channel5.send(5L)
        classC.channel6.receive()
    }
    
    launch {
        channel7.send(classC.channel5.receive())
        classC.channel6.send(3.14f)
    }
    
    launch {
        channel8.receive()
    }
    
    launch {
        channel7.close()
    }
}

fun main(): Unit = runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    
    functionA(classA)
    functionB(classB)
    functionC(classC)
}

class RunChecker421: RunCheckerBase() {
    override fun block() = main()
}