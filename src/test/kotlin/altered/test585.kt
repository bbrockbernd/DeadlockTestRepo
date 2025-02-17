/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test585
import org.example.altered.test585.RunChecker585.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel = Channel<Int>()

    fun functionA() = runBlocking(pool) {
        coroutineA()
        coroutineB()
        coroutineC()
    }

    suspend fun coroutineA() {
        coroutineScope {
            launch(pool) {
                channel.send(1)
                channel.receive()
            }
        }
    }

    suspend fun coroutineB() {
        coroutineScope {
            launch(pool) {
                channel.send(2)
                channel.receive()
            }
        }
    }

    suspend fun coroutineC() {
        coroutineScope {
            launch(pool) {
                channel.receive()
                channel.send(3)
            }
        }
    }
}

fun main(): Unit{
    DeadlockExample().functionA()
}

class RunChecker585: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}