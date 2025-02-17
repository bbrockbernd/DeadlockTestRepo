/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":3,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test292
import org.example.altered.test292.RunChecker292.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
}

fun func1(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            manager.ch1.send(1)
        }
        launch(pool) {
            println(manager.ch2.receive())
        }
    }
}

fun func2(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            manager.ch3.send(2)
        }
        launch(pool) {
            println(manager.ch4.receive())
        }
    }
}

fun func3(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            manager.ch5.send(3)
        }
        launch(pool) {
            println(manager.ch6.receive())
        }
    }
}

fun func4(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            val data = manager.ch1.receive() + 4
            manager.ch7.send(data)
        }
    }
}

fun func5(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            val data = manager.ch3.receive() + 5
            manager.ch2.send(data)
        }
    }
}

fun func6(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            val data = manager.ch5.receive() + 6
            manager.ch4.send(data)
        }
    }
}

fun func7(manager: ChannelManager) {
    runBlocking(pool) {
        launch(pool) {
            val data = manager.ch7.receive() + 7
            manager.ch6.send(data)
        }
    }
}

fun main(): Unit{
    val manager = ChannelManager()
    runBlocking(pool) {
        launch(pool) { func1(manager) }
        launch(pool) { func2(manager) }
        launch(pool) { func3(manager) }
        launch(pool) { func4(manager) }
        launch(pool) { func5(manager) }
        launch(pool) { func6(manager) }
        launch(pool) { func7(manager) }
    }
}

class RunChecker292: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}