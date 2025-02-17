/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test986
import org.example.altered.test986.RunChecker986.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>(1)

    launch(pool) { 
        sendValue(channel) 
    }
    launch(pool) { 
        receiveAndProcess(channel) 
    }
    launch(pool) { 
        printResult(channel) 
    }
}

suspend fun sendValue(channel: Channel<Int>) {
    delay(500) // Simulate some work
    channel.send(42)
}

suspend fun receiveAndProcess(channel: Channel<Int>) {
    val value = channel.receive()
    val processedValue = processValue(value)
    channel.send(processedValue)
}

suspend fun printResult(channel: Channel<Int>) {
    val result = channel.receive()
    println("Result: $result")
}

suspend fun processValue(value: Int): Int {
    delay(500) // Simulate some processing time
    return value * 2
}

class RunChecker986: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}