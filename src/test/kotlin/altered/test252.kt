/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.altered.test252
import org.example.altered.test252.RunChecker252.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun func1(channel1: SendChannel<Int>, channel2: ReceiveChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.send(1)
            delay(100)
            channel2.receive()
        }
    }
}

fun func2(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.receive()
            delay(100)
            channel2.send(2)
        }
    }
}

fun func3(channel1: SendChannel<Int>, channel2: ReceiveChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.send(3)
            delay(100)
            channel2.receive()
        }
    }
}

fun func4(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.receive()
            delay(100)
            channel2.send(4)
        }
    }
}

fun func5(channel1: SendChannel<Int>, channel2: ReceiveChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.send(5)
            delay(100)
            channel2.receive()
        }
    }
}

fun func6(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.receive()
            delay(100)
            channel2.send(6)
        }
    }
}

fun func7(channel1: SendChannel<Int>, channel2: ReceiveChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.send(7)
            delay(100)
            channel2.receive()
        }
    }
}

fun func8(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.receive()
            delay(100)
            channel2.send(8)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    func1(channel1, channel2)
    func2(channel1, channel2)
    func3(channel1, channel2)
    func4(channel1, channel2)
    func5(channel1, channel2)
    func6(channel1, channel2)
    func7(channel1, channel2)
    func8(channel1, channel2)
}

class RunChecker252: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}