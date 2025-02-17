/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test452
import org.example.altered.test452.RunChecker452.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun sendData(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            ch1.send(1)
            ch2.send(2)
        }
        launch(pool) {
            ch3.send(3)
            ch4.send(4)
        }
    }
}

fun processData(ch5: Channel<Int>, ch6: Channel<Int>, ch7: Channel<Int>, ch8: Channel<Int>): Int {
    var result = 0
    runBlocking(pool) {
        launch(pool) {
            result += ch5.receive() + ch6.receive()
        }
        launch(pool) {
            result += ch7.receive() + ch8.receive()
        }
        launch(pool) {
            ch5.send(1)
            ch6.send(2)
            ch7.send(3)
            ch8.send(4)
        }
    }
    return result
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
        launch(pool) { sendData(ch1, ch2, ch3, ch4) }
        launch(pool) { processData(ch5, ch6, ch7, ch8) }
    }
}

class RunChecker452: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}