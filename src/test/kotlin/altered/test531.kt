/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test531
import org.example.altered.test531.RunChecker531.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

class ClassA(val channelA: Channel<Int>, val channelB: Channel<Int>)
class ClassB(val channelC: Channel<Int>)
class ClassC(val classA: ClassA, val classB: ClassB)

fun functionA(channelA: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channelA.send(1)
        }
    }
}

fun functionB(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val value = channelB.receive()
            channelC.send(value + 1)
        }
    }
}

fun functionC(classA: ClassA, classB: ClassB): Int {
    return runBlocking(pool) {
        var result = 0
        launch(pool) {
            result = classB.channelC.receive() + classA.channelB.receive()
        }
        result
    }
}

suspend fun functionD(classC: ClassC): Int {
    return coroutineScope {
        val resultA = classC.classA.channelA.receive()
        val resultB = functionC(classC.classA, classC.classB)
        resultA + resultB
    }
}

fun main(): Unit{
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)
    val channelC = Channel<Int>(1)

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelC)
    val classC = ClassC(classA, classB)

    runBlocking(pool) {
        launch(pool) { functionA(channelA) }
        launch(pool) { functionB(channelB, channelC) }
        val finalResult = functionD(classC)
        println("Final Result: $finalResult")
    }
}

class RunChecker531: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}