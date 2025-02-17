/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.altered.test345
import org.example.altered.test345.RunChecker345.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)
class B(val channel2: Channel<Int>, val channel3: Channel<Int>)
class C(val channel3: Channel<Int>, val channel1: Channel<Int>)

fun func1(a: A) {
    runBlocking(pool) {
        launch(pool) {
            val received = a.channel1.receive()
            a.channel2.send(received + 1)
        }
    }
}

fun func2(b: B) {
    runBlocking(pool) {
        launch(pool) {
            val received = b.channel2.receive()
            b.channel3.send(received + 2)
        }
    }
}

fun func3(c: C) {
    runBlocking(pool) {
        launch(pool) {
            val received = c.channel3.receive()
            c.channel1.send(received + 3)
        }
    }
}

fun func4(a: A, b: B, c: C) {
    runBlocking(pool) {
        launch(pool) {
            a.channel1.send(1)
            val received = a.channel2.receive()
            b.channel2.send(received)
            val finalReceived = c.channel1.receive()
            println(finalReceived)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel2, channel3)
    val c = C(channel3, channel1)

    func1(a)
    func2(b)
    func3(c)
    func4(a, b, c)
}

class RunChecker345: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}