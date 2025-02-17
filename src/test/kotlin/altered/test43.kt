/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test43
import org.example.altered.test43.RunChecker43.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExampleClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    fun function1() {
        runBlocking(pool) {
            launch(pool) {
                function2()
            }
            launch(pool) {
                function3()
            }
        }
    }

    fun function2() {
        runBlocking(pool) {
            launch(pool) {
                val received = channel1.receive()
                channel2.send(received)
            }
            launch(pool) {
                val num = 5
                channel1.send(num)
            }
        }
    }

    fun function3() {
        runBlocking(pool) {
            launch(pool) {
                val received = channel2.receive()
                println("Received in function3: $received")
                function4()
            }
        }
    }

    fun function4() {
        runBlocking(pool) {
            launch(pool) {
                val value = 10
                channel1.send(value)
                val received = channel1.receive()
                println("Received in function4: $received")
            }
        }
    }
}

fun main(): Unit{
    val example = ExampleClass()
    example.function1()
}

class RunChecker43: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}