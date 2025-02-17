/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test416
import org.example.altered.test416.RunChecker416.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    
    suspend fun sendInClassA() {
        for (i in 0 until 5) {
            channelA.send(i)
        }
        for (i in 5 until 10) {
            channelB.send(i)
        }
    }
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    
    suspend fun sendInClassB() {
        for (i in 10 until 15) {
            channelC.send(i)
        }
        for (i in 15 until 20) {
            channelD.send(i)
        }
    }
}

class ClassC {
    val channelE = Channel<Int>()
    
    suspend fun receiveInClassC(channelA: Channel<Int>, channelB: Channel<Int>) {
        for (i in 0 until 5) {
            channelE.send(channelA.receive())
        }
        for (i in 5 until 10) {
            channelE.send(channelB.receive())
        }
    }
}

class ClassD {
    fun performOperations(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>, channelD: Channel<Int>, channelE: Channel<Int>) {
        runBlocking(pool) {
            val job1 = launch(pool) { ClassA().sendInClassA() }
            val job2 = launch(pool) { ClassB().sendInClassB() }
            val job3 = launch(pool) { ClassC().receiveInClassC(channelA, channelB) }
            val job4 = launch(pool) {
                for (i in 0 until 10) {
                    channelC.send(channelE.receive())
                }
                for (i in 10 until 20) {
                    channelD.send(channelE.receive())
                }
            }
        }
    }
}

fun main(): Unit{
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    val classD = ClassD()

    classD.performOperations(classA.channelA, classA.channelB, classB.channelC, classB.channelD, classC.channelE)
}

class RunChecker416: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}