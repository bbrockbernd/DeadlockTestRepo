/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
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
package org.example.altered.test202
import org.example.altered.test202.RunChecker202.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA: Channel<Int>, val channelB: Channel<Int>)
class ClassB(val channelC: Channel<Int>, val channelD: Channel<Int>)
class ClassC(val channelE: Channel<Int>, val channelF: Channel<Int>, val channelG: Channel<Int>)

fun function1(classA: ClassA, classB: ClassB) = runBlocking(pool) {
    launch(pool) {
        classA.channelA.send(1)
        val received = classB.channelC.receive()
        println("Function1 received: $received")
        classA.channelB.send(received)
    }
}

suspend fun function2(classB: ClassB, classC: ClassC) {
    classB.channelC.send(2)
    val received = classC.channelE.receive()
    println("Function2 received: $received")
    classB.channelD.send(received)
}

suspend fun function3(classC: ClassC, classA: ClassA) {
    classC.channelE.send(3)
    val received = classA.channelA.receive()
    println("Function3 received: $received")
    classC.channelF.send(received)
}

fun function4(classC: ClassC) = runBlocking(pool) {
    classC.channelG.send(4)
    val received = classC.channelF.receive()
    println("Function4 received: $received")
    classC.channelG.send(received)
}

fun main(): Unit = runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>(1)
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelC, channelD)
    val classC = ClassC(channelE, channelF, channelG)

    launch(pool) { function1(classA, classB) }
    launch(pool) { function2(classB, classC) }
    launch(pool) { function3(classC, classA) }
    function4(classC)
}

class RunChecker202: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}