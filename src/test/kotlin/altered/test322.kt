/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.altered.test322
import org.example.altered.test322.RunChecker322.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val value = channel.receive()
            println("Received in function1: $value")
        }
    }
}

fun function2(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val value = channel.receive()
            println("Received in function2: $value")
        }
    }
}

fun function3(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel.send(1)
            println("Sent 1 in function3")
        }
    }
}

fun function4(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel.send(2)
            println("Sent 2 in function4")
        }
    }
}

fun function5(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val value = channel.receive()
            println("Received in function5: $value")
        }
    }
}

fun function6(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel.send(3)
            println("Sent 3 in function6")
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()

    runBlocking(pool) {
        launch(pool) { function1(channel) }
        launch(pool) { function2(channel) }
        launch(pool) { function3(channel) }
        launch(pool) { function4(channel) }
        launch(pool) { function5(channel) }
        launch(pool) { function6(channel) }
        launch(pool) {
            val value = channel.receive()
            println("Received in main: $value")
        }
    }
}

class RunChecker322: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}