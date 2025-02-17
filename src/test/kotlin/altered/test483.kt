/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":4,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 4 different coroutines
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
package org.example.altered.test483
import org.example.altered.test483.RunChecker483.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel3, channel4, channel5) }
    launch(pool) { function3(channel6, channel7) }
    launch(pool) { function4(channel8, channel1, channel6) }

    channel2.send(5)
    channel4.send(10)
    channel7.send(15)
    function5(channel5, channel8)
    function6(channel3, channel2)
}

suspend fun function1(ch1: Channel<Int>, ch2: Channel<Int>) {
    val value = ch2.receive()
    ch1.send(value + 1)
}

suspend fun function2(ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>) {
    val value = ch4.receive()
    ch5.send(value * 2)
    ch3.send(value - 1)
}

suspend fun function3(ch6: Channel<Int>, ch7: Channel<Int>) {
    val value = ch7.receive()
    ch6.send(value / 3)
}

suspend fun function4(ch8: Channel<Int>, ch1: Channel<Int>, ch6: Channel<Int>) {
    val value = ch1.receive()
    ch8.send(value + 4)
    ch6.receive()
}

suspend fun function5(ch5: Channel<Int>, ch8: Channel<Int>) {
    val value5 = ch5.receive()
    ch8.send(value5 - 2)
}

suspend fun function6(ch3: Channel<Int>, ch2: Channel<Int>) {
    val value = ch3.receive()
    ch2.send(value + 3)
}

class RunChecker483: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}