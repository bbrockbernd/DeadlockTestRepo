/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test293
import org.example.altered.test293.RunChecker293.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample(private val ch1: Channel<Int>, private val ch2: Channel<Int>, private val ch3: Channel<Int>) {
    suspend fun function1() {
        ch1.send(1)
        ch2.receive()
    }

    suspend fun function2() {
        ch2.send(2)
        ch3.receive()
    }

    suspend fun function3() {
        ch3.send(3)
        ch1.receive()
    }
}

suspend fun function4(ch4: Channel<Int>, ch5: Channel<Int>) {
    ch4.send(4)
    ch5.receive()
}

suspend fun function5(ch5: Channel<Int>, ch6: Channel<Int>) {
    ch5.send(5)
    ch6.receive()
}

fun main(): Unit= runBlocking(pool) {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val example = DeadlockExample(ch1, ch2, ch3)

    launch(pool) { example.function1() }
    launch(pool) { example.function2() }
    launch(pool) { example.function3() }
    launch(pool) { function4(ch4, ch5) }
    launch(pool) { function5(ch5, ch6) }
    
    // To create more deadlocks, add more dependencies on the channels
    launch(pool) {
        ch8.send(8)
        ch7.receive()
    }
    launch(pool) {
        ch7.send(7)
        ch8.receive()
    }
}

class RunChecker293: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}