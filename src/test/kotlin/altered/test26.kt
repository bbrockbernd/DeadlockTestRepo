/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.altered.test26
import org.example.altered.test26.RunChecker26.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channelA: Channel<Int>)
class ClassB(val channelB: Channel<Int>)
class ClassC(val channelC: Channel<Int>)
class ClassD(val channelD: Channel<Int>)
class ClassE(val channelE: Channel<Int>)

fun function1(classA: ClassA, classB: ClassB, classC: ClassC) = runBlocking(pool) {
    launch(pool) {
        val valueA = classA.channelA.receive()
        classB.channelB.send(valueA)
    }
    launch(pool) {
        val valueC = classC.channelC.receive()
        classA.channelA.send(valueC)
    }
}

fun function2(classB: ClassB, classC: ClassC, classD: ClassD) = runBlocking(pool) {
    launch(pool) {
        val valueB = classB.channelB.receive()
        classC.channelC.send(valueB)
    }
    launch(pool) {
        val valueD = classD.channelD.receive()
        classB.channelB.send(valueD)
    }
}

fun function3(classD: ClassD, classE: ClassE, classA: ClassA) = runBlocking(pool) {
    launch(pool) {
        val valueE = classE.channelE.receive()
        classD.channelD.send(valueE)
    }
    launch(pool) {
        val valueA = classA.channelA.receive()
        classE.channelE.send(valueA)
    }
}

fun function4(classE: ClassE, classB: ClassB, classD: ClassD) = runBlocking(pool) {
    launch(pool) {
        val valueB = classB.channelB.receive()
        classE.channelE.send(valueB)
    }
    launch(pool) {
        val valueD = classD.channelD.receive()
        classB.channelB.send(valueD)
    }
}

val channelA = Channel<Int>()
val channelB = Channel<Int>()
val channelC = Channel<Int>()
val channelD = Channel<Int>()
val channelE = Channel<Int>()

val classA = ClassA(channelA)
val classB = ClassB(channelB)
val classC = ClassC(channelC)
val classD = ClassD(channelD)
val classE = ClassE(channelE)

fun main(): Unit= runBlocking(pool) {
    launch(pool) { function1(classA, classB, classC) }
    launch(pool) { function2(classB, classC, classD) }
    launch(pool) { function3(classD, classE, classA) }
    launch(pool) { function4(classE, classB, classD) }
}

class RunChecker26: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}