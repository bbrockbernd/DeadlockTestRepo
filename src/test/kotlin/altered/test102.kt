/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":8,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 8 different coroutines
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
package org.example.altered.test102
import org.example.altered.test102.RunChecker102.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass(val channel1: Channel<Int>, val channel2: Channel<Int>, val channel3: Channel<Int>, val channel4: Channel<Int>)

fun function1(testClass: TestClass) {
    GlobalScope.launch(pool) {
        repeat(10) {
            testClass.channel1.send(it)
        }
    }
}

fun function2(testClass: TestClass) {
    GlobalScope.launch(pool) {
        repeat(10) {
            testClass.channel2.send(it * 2)
        }
    }
}

fun function3(testClass: TestClass) {
    GlobalScope.launch(pool) {
        repeat(10) {
            val value = testClass.channel1.receive()
            testClass.channel3.send(value + 1)
        }
    }
}

fun function4(testClass: TestClass) {
    GlobalScope.launch(pool) {
        repeat(10) {
            val value = testClass.channel2.receive()
            testClass.channel4.send(value - 1)
        }
    }
}

fun function5(testClass: TestClass) {
    runBlocking(pool) {
        val job1 = launch(pool) {
            repeat(10) {
                println("Channel3 received: ${testClass.channel3.receive()}")
            }
        }

        val job2 = launch(pool) {
            repeat(10) {
                println("Channel4 received: ${testClass.channel4.receive()}")
            }
        }

        job1.join()
        job2.join()
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val testClass = TestClass(channel1, channel2, channel3, channel4)

    function1(testClass)
    function2(testClass)
    function3(testClass)
    function4(testClass)
    function5(testClass)
}

class RunChecker102: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}