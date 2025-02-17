/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 2 different coroutines
- 4 different classes

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
package org.example.altered.test194
import org.example.altered.test194.RunChecker194.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>)

class ClassB(val ch3: Channel<Int>)

class ClassC(val ch4: Channel<Int>)

class ClassD(val ch5: Channel<Int>)

fun fun1(classA: ClassA, classC: ClassC) {
    CoroutineScope(pool).launch(pool) {
        repeat(5) {
            classA.ch1.send(it)
        }
    }

    CoroutineScope(pool).launch(pool) {
        repeat(5) {
            classC.ch4.send(it * 2)
        }
    }
}

suspend fun fun2(classA: ClassA, classB: ClassB) {
    repeat(5) {
        val receivedValue = classA.ch1.receive()
        classB.ch3.send(receivedValue + 1)
    }
}

suspend fun fun3(classB: ClassB, classD: ClassD) {
    repeat(5) {
        val receivedValue = classB.ch3.receive()
        classD.ch5.send(receivedValue * 2)
    }
}

fun fun4(classC: ClassC, classD: ClassD) {
    CoroutineScope(pool).launch(pool) {
        repeat(5) {
            val receivedValue = classC.ch4.receive()
            println("ClassC received: $receivedValue")
        }
    }

    CoroutineScope(pool).launch(pool) {
        repeat(5) {
            val receivedValue = classD.ch5.receive()
            println("ClassD received: $receivedValue")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val classA = ClassA(ch1, ch2)
    val classB = ClassB(ch3)
    val classC = ClassC(ch4)
    val classD = ClassD(ch5)

    fun1(classA, classC)

    launch(pool) {
        fun2(classA, classB)
    }

    launch(pool) {
        fun3(classB, classD)
    }

    fun4(classC, classD)
}

class RunChecker194: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}