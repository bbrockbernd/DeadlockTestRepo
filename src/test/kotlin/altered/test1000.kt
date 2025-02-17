/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 5 different coroutines
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
- lists, arrays or other datastructures
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
package org.example.altered.test1000
import org.example.altered.test1000.RunChecker1000.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Channel1(val channel: Channel<Int>)
class Channel2(val channel: Channel<Int>)
class Channel3(val channel: Channel<Int>)

fun runDeadlockTest() = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val ch1 = Channel1(channel1)
    val ch2 = Channel2(channel2)
    val ch3 = Channel3(channel3)

    launch(pool) { coroutine1(ch1, ch2) }
    launch(pool) { coroutine2(ch2, ch3) }
    launch(pool) { coroutine3(ch3, ch1) }
    launch(pool) { coroutine4(ch1) }
    launch(pool) { coroutine5(ch2) }
}

suspend fun coroutine1(ch1: Channel1, ch2: Channel2) {
    ch1.channel.receive()
    ch2.channel.send(1)
}

suspend fun coroutine2(ch2: Channel2, ch3: Channel3) {
    ch2.channel.receive()
    ch3.channel.send(2)
}

suspend fun coroutine3(ch3: Channel3, ch1: Channel1) {
    ch3.channel.receive()
    ch1.channel.send(3)
}

suspend fun coroutine4(ch1: Channel1) {
    ch1.channel.send(4)
}

suspend fun coroutine5(ch2: Channel2) {
    ch2.channel.send(5)
}

fun main(): Unit{
    runDeadlockTest()
}

class RunChecker1000: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}