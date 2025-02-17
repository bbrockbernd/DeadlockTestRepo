/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
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
package org.example.altered.test172
import org.example.altered.test172.RunChecker172.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()

    val job1 = launch(pool) { coroutine1(channel) }
    val job2 = launch(pool) { coroutine2(channel) }
    val job3 = launch(pool) { coroutine3(channel) }
    val job4 = launch(pool) { coroutine4(channel) }

    job1.join()
    job2.join()
    job3.join()
    job4.join()
}

suspend fun coroutine1(channel: Channel<Int>) {
    functionA(channel)
}

suspend fun coroutine2(channel: Channel<Int>) {
    functionB(channel)
}

suspend fun coroutine3(channel: Channel<Int>) {
    functionC(channel)
}

suspend fun coroutine4(channel: Channel<Int>) {
    functionD(channel)
}

suspend fun functionA(channel: Channel<Int>) {
    channel.send(1)
    functionE(channel)
}

suspend fun functionB(channel: Channel<Int>) {
    functionF(channel)
    channel.send(2)
}

suspend fun functionC(channel: Channel<Int>) {
    val value = channel.receive()
    println("Received in functionC: $value")
}

suspend fun functionD(channel: Channel<Int>) {
    val value = channel.receive()
    println("Received in functionD: $value")
}

suspend fun functionE(channel: Channel<Int>) {
    val value = channel.receive()
    println("Received in functionE: $value")
}

suspend fun functionF(channel: Channel<Int>) {
    channel.send(3)
    val value = channel.receive()
    println("Received in functionF: $value")
}

class RunChecker172: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}