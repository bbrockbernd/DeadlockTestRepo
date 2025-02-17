/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":8,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 8 different coroutines
- 2 different classes

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
package org.example.altered.test422
import org.example.altered.test422.RunChecker422.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            ch1.send(i)
            ch2.send(i * 2)
        }
    }
}

class Consumer(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..3) {
            val v1 = ch1.receive()
            val v2 = ch2.receive()
        }
    }
}

fun intermediateFunction(ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>, ch6: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val temp = ch3.receive()
            ch4.send(temp)
        }
        launch(pool) {
            val temp = ch4.receive()
            ch5.send(temp)
        }
        launch(pool) {
            val temp = ch5.receive()
            ch6.send(temp)
        }
    }
}

fun main(): Unit{
    runBlocking(pool) {
        val ch1 = Channel<Int>()
        val ch2 = Channel<Int>()
        val ch3 = Channel<Int>()
        val ch4 = Channel<Int>()
        val ch5 = Channel<Int>()
        val ch6 = Channel<Int>()
        val ch7 = Channel<Int>()

        val producer = Producer(ch1, ch2)
        val consumer = Consumer(ch3, ch4)

        launch(pool) { producer.produce() }
        launch(pool) {
            repeat(3) {
                val x = ch1.receive()
                ch3.send(x)
            }
        }

        launch(pool) { intermediateFunction(ch3, ch4, ch5, ch6) }

        launch(pool) {
            repeat(3) {
                val y = ch5.receive()
                ch7.send(y)
            }
        }

        launch(pool) { consumer.consume() }

        repeat(3) {
            launch(pool) {
                val z = ch6.receive()
                println("Received: $z")
            }
        }
    }
}

class RunChecker422: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}