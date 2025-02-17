/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test387
import org.example.altered.test387.RunChecker387.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ExampleClass {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<String>()

    fun function1() = runBlocking(pool) {
        launch(pool) { producer1() }
        launch(pool) { producer2() }
        launch(pool) { consumer() }
    }

    private suspend fun producer1() {
        repeat(5) {
            channel1.send(it)
            delay(100L)
        }
        channel1.close()
    }

    private suspend fun producer2() {
        repeat(5) {
            channel2.send(it * 2)
            delay(150L)
        }
        channel2.close()
    }

    private suspend fun consumer() {
        coroutineScope {
            launch(pool) { consumeChannel1() }
            launch(pool) { consumeChannel2() }
        }
    }

    private suspend fun consumeChannel1() {
        for (value in channel1) {
            channel3.send("Channel1: $value")
        }
    }

    private suspend fun consumeChannel2() {
        for (value in channel2) {
            channel3.send("Channel2: $value")
        }
        channel3.close()
    }

    fun function2() = runBlocking(pool) {
        launch(pool) { receiver() }
    }

    private suspend fun receiver() {
        for (msg in channel3) {
            println(msg)
        }
    }
}

fun main(): Unit{
    ExampleClass().function1()
    ExampleClass().function2()
}

class RunChecker387: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}