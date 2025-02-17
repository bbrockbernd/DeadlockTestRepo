/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
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
package org.example.altered.test545
import org.example.altered.test545.RunChecker545.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelContainer {
    val channel = Channel<Int>()
}

fun producer(container: ChannelContainer) {
    runBlocking(pool) {
        container.channel.send(1)
        container.channel.send(2)
    }
}

suspend fun intermediateFunction(container: ChannelContainer) {
    coroutineScope {
        launch(pool) {
            val value = container.channel.receive() // This line will cause a deadlock if there are no items sent by producer
            println("Received $value")
        }
    }
}

suspend fun consumer(container: ChannelContainer) {
    intermediateFunction(container)
}

fun main(): Unit{
    val container = ChannelContainer()

    runBlocking(pool) {
        launch(pool) {
            consumer(container)
        }

        launch(pool) {
            producer(container)
        }
    }
}

class RunChecker545: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}