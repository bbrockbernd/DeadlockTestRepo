/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":7,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test187
import org.example.altered.test187.RunChecker187.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        repeat(3) {
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer1 {
    suspend fun consume(channel: Channel<Int>) {
        for (v in channel) {
            println("Consumer1 received $v")
        }
    }
}

class Consumer2 {
    suspend fun consume(channel: Channel<Int>) {
        for (v in channel) {
            println("Consumer2 received $v")
        }
    }
}

class Coordinator {
    fun start() {
        val channel = Channel<Int>()

        runBlocking(pool) {
            coroutineScope {
                launch(pool) { Producer().produce(channel) }
                launch(pool) { consumer1(channel) }
                launch(pool) { consumer2(channel) }
            }
        }
    }

    private suspend fun consumer1(channel: Channel<Int>) {
        val obj = Consumer1()
        obj.consume(channel)
    }

    private suspend fun consumer2(channel: Channel<Int>) {
        val obj = Consumer2()
        obj.consume(channel)
    }
}

fun function1(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val obj = Coordinator()
            obj.start()
        }
        launch(pool) { Producer().produce(channel) }
    }
}

fun function2(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            coroutineScope {
                val obj = Consumer1()
                obj.consume(channel)
            }
        }
    }
}

fun function3(channel: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) { Consumer2().consume(channel) }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    function1(channel)
    function2(channel)
    function3(channel)
}

class RunChecker187: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}