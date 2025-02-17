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
import org.example.altered.test421.RunChecker421.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
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

fun functionA(classA: ClassA) = runBlocking(pool) {
    val channel7 = Channel<Int>()
    val channel8 = Channel<Unit>()

    launch(pool) {
        classA.channel1.send(1)
        classA.channel2.receive()
    }
    
    launch(pool) {
        channel7.send(classA.channel1.receive())
        classA.channel2.send(2)
    }
    
    launch(pool) {
        channel8.receive()
    }
    
    launch(pool) {
        channel7.close()
    }
}

fun functionB(classB: ClassB) = runBlocking(pool) {
    val channel7 = Channel<String>()
    val channel8 = Channel<Double>()

    launch(pool) {
        classB.channel3.send("test")
        classB.channel4.receive()
    }
    
    launch(pool) {
        channel7.send(classB.channel3.receive())
        classB.channel4.send(3.14)
    }
    
    launch(pool) {
        channel8.receive()
    }
    
    launch(pool) {
        channel7.close()
    }
}

fun functionC(classC: ClassC) = runBlocking(pool) {
    val channel7 = Channel<Long>()
    val channel8 = Channel<Float>()

    launch(pool) {
        classC.channel5.send(5L)
        classC.channel6.receive()
    }
    
    launch(pool) {
        channel7.send(classC.channel5.receive())
        classC.channel6.send(3.14f)
    }
    
    launch(pool) {
        channel8.receive()
    }
    
    launch(pool) {
        channel7.close()
    }
}

fun main(): Unit = runBlocking(pool) {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    
    functionA(classA)
    functionB(classB)
    functionC(classC)
}

class RunChecker421: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}