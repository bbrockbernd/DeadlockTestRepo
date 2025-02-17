/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test922
import org.example.altered.test922.RunChecker922.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    
    suspend fun sendToA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<Int>()
    
    suspend fun sendToB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromB(): Int {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Int>()
    
    suspend fun sendToC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromC(): Int {
        return channelC.receive()
    }
}

fun main(): Unit= runBlocking(pool) {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    
    val channelD = Channel<Int>()
    
    launch(pool) {
        classA.sendToA(1)
        classB.receiveFromB()
    }
    
    launch(pool) {
        classB.sendToB(2)
        classC.receiveFromC()
    }
    
    launch(pool) {
        classC.sendToC(3)
        channelD.receive()
    }
    
    launch(pool) {
        channelD.send(4)
        classA.receiveFromA()
    }
    
    // Intentionally never running, it will cause this program to deadlock
    // launch(pool) {
    //     delay(1000)
    //     channelD.send(1)
    // }
}

class RunChecker922: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}