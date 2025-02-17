/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test311
import org.example.altered.test311.RunChecker311.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun functionA() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

class ClassB(private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun functionB() {
        val value = channel3.receive()
        channel4.send(value)
    }
}

class ClassC(private val channel1: Channel<Int>, private val channel3: Channel<Int>) {
    suspend fun functionC() {
        val value = channel1.receive()
        channel3.send(value)
    }
}

suspend fun function1(a: ClassA) {
    a.functionA()
}

suspend fun function2(b: ClassB) {
    b.functionB()
}

suspend fun function3(c: ClassC) {
    c.functionC()
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel1, channel3)

    launch(pool) {
        function1(classA)
    }

    launch(pool) {
        function2(classB)
    }

    launch(pool) {
        function3(classC)
    }

    channel1.send(10)
    channel4.receive()
}

class RunChecker311: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}