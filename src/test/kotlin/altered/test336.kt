/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test336
import org.example.altered.test336.RunChecker336.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(ch1: Channel<Int>, ch2: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            ch1.send(1)
            ch2.receive()
        }
    }
}

fun function2(ch3: Channel<Int>, ch4: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            ch3.send(2)
            ch4.receive()
        }
    }
}

fun function3(ch5: Channel<Int>, ch6: Channel<Int>, ch7: Channel<Int>, ch8: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            ch5.send(3)
            ch6.receive()
        }
        launch(pool) {
            ch7.send(4)
            ch8.receive()
        }
        launch(pool) {
            ch8.send(5)
            ch7.receive()
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    runBlocking(pool) {
        launch(pool) { function1(ch1, ch2) }
        launch(pool) { function2(ch3, ch4) }
        launch(pool) { function3(ch5, ch6, ch7, ch8) }

        launch(pool) {
            val fromCh1 = ch1.receive()
            val fromCh3 = ch3.receive()
            val fromCh5 = ch5.receive()
            println("Received from channels: $fromCh1, $fromCh3, $fromCh5")
        }
    }
}

class RunChecker336: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}