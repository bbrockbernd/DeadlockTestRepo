/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.altered.test392
import org.example.altered.test392.RunChecker392.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel = Channel<Int>()

fun function1() {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) { function2() }
            launch(pool) { function3() }
            launch(pool) { function4() }
            launch(pool) { function5() }
        }
    }
}

suspend fun function2() {
    val value = channel.receive()
    function6(value)
}

suspend fun function3() {
    val value = 1
    channel.send(value)
}

suspend fun function4() {
    val value = channel.receive()
    function7(value)
}

suspend fun function5() {
    val value = 2
    channel.send(value)
}

suspend fun function6(value: Int) {
    println("Received in function6: $value")
}

suspend fun function7(value: Int) {
    println("Received in function7: $value")
}

class ClassA {
    fun execute() {
        function1()
    }
}

class ClassB {
    fun execute() {
        function1()
    }
}

fun main(): Unit{
    val instanceA = ClassA()
    val instanceB = ClassB()
    instanceA.execute()
    instanceB.execute()
}

class RunChecker392: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}