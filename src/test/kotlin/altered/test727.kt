/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test727
import org.example.altered.test727.RunChecker727.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun funcA() {
        val result = channel1.receive()
        channel2.send(result)
    }
}

class ClassB(val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun funcB() {
        val result = channel2.receive()
        channel3.send(result)
    }
}

class ClassC(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun funcC() {
        val result = channel3.receive()
        channel4.send(result)
    }
}

suspend fun mainFunction() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel2, channel3)
    val classC = ClassC(channel3, channel4)

    runBlocking(pool) {
        launch(pool) {
            classA.funcA()
        }
        launch(pool) {
            classB.funcB()
        }
        launch(pool) {
            classC.funcC()
        }

        channel1.send(1)
        channel4.receive()
    }
}

fun main(): Unit= runBlocking(pool) {
    mainFunction()
}

class RunChecker727: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}