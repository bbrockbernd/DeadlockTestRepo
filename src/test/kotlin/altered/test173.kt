/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 3 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.altered.test173
import org.example.altered.test173.RunChecker173.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()

suspend fun function1() {
    val result = channel1.receive()
    channel2.send(result)
}

suspend fun function2() {
    val result = channel2.receive()
    channel3.send(result)
}

suspend fun function3() {
    val result = channel3.receive()
    channel1.send(result)
}

suspend fun function4() {
    channel1.send(10)
    function1()
}

suspend fun function5() {
    function2()
}

suspend fun function6() {
    function3()
}

fun function7() = runBlocking(pool) {
    launch(pool) {
        function4()
    }
    launch(pool) {
        function5()
    }
    launch(pool) {
        function6()
    }
}

fun function8() {
    function7()
}

fun main(): Unit{
    function8()
}

class RunChecker173: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}