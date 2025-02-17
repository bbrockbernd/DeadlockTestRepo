/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
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
package org.example.altered.test984
import org.example.altered.test984.RunChecker984.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel: Channel<Int>) {
    suspend fun produceData() {
        channel.send(1)
        channel.send(2)
        channel.send(3)
    }

    suspend fun consumeData() {
        repeat(3) {
            println("Consumed: ${channel.receive()}")
        }
    }
}

fun functionOne(channel: Channel<Int>) {
    val processor = Processor(channel)
    runBlocking(pool) {
        coroutineScope {
            launch(pool) { processor.produceData() }
            launch(pool) { processor.consumeData() }
        }
    }
}

fun functionTwo(channel: Channel<Int>) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) { channel.send(4) }
            launch(pool) { println("Received: ${channel.receive()}") }
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()

    runBlocking(pool) {
        launch(pool) { functionOne(channel) }
        launch(pool) { functionTwo(channel) }
        launch(pool) { channel.send(5) }
        launch(pool) { println("Received in main: ${channel.receive()}") }
        launch(pool) { println("Received in main: ${channel.receive()}") }
    }
}

class RunChecker984: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}