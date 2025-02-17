/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":1,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 1 different coroutines
- 1 different classes

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
package org.example.altered.test3
import org.example.altered.test3.RunChecker3.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass(val c1: Channel<Int>, val c2: Channel<Int>) {
    suspend fun function1() = c1.send(1)
    suspend fun function2() = c2.send(2)
    suspend fun function3() = c2.receive() + c1.receive()
}

suspend fun function4(c3: Channel<Int>, c4: Channel<Int>): Int {
    c3.send(4)
    c4.send(5)
    return c4.receive() + c3.receive()
}

suspend fun function5(c5: Channel<Int>, c6: Channel<Int>): Int {
    c5.send(6)
    c6.send(7)
    return c5.receive() + c6.receive()
}

suspend fun function6(c7: Channel<Int>): Int {
    c7.send(8)
    return c7.receive()
}

suspend fun function7(tc: TestClass): Int {
    tc.function1()
    tc.function2()
    return tc.function3()
}

fun main(): Unit= runBlocking(pool) {
    val c1 = Channel<Int>()
    val c2 = Channel<Int>()
    val c3 = Channel<Int>()
    val c4 = Channel<Int>()
    val c5 = Channel<Int>()
    val c6 = Channel<Int>()
    val c7 = Channel<Int>()
    val c8 = Channel<Int>()

    val testClass = TestClass(c1, c2)

    launch(pool) {
        val result1 = function4(c3, c4)
        val result2 = function5(c5, c6)
        val result3 = function6(c7)
        val result4 = function7(testClass)
        
        c8.send(result1 + result2 + result3 + result4)
    }

    println("Final result: ${c8.receive()}")
}

class RunChecker3: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}