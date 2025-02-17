/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
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
package org.example.altered.test732
import org.example.altered.test732.RunChecker732.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun processChannels(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>) {
    ch1.send(1)
    val value = ch1.receive()
    ch2.send(value)
    ch3.send(ch2.receive() + 1)
    ch4.send(ch3.receive() * 2)
    ch5.send(ch4.receive() - 1)
    println("Final value: ${ch5.receive()}")
}

fun main(): Unit= runBlocking(pool) {
    val ch1 = Channel<Int>(1)
    val ch2 = Channel<Int>(1)
    val ch3 = Channel<Int>(1)
    val ch4 = Channel<Int>(1)
    val ch5 = Channel<Int>(1)

    launch(pool) { processChannels(ch1, ch2, ch3, ch4, ch5) }
    launch(pool) { ch1.send(2) }
    launch(pool) { println("Channel 2 received: ${ch2.receive()}") }
    launch(pool) { println("Channel 4 processed: ${ch4.receive()}") }
    launch(pool) { ch5.send(7) }
}


class RunChecker732: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}