/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test733
import org.example.altered.test733.RunChecker733.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val chanX: Channel<Int>, private val chanY: Channel<Int>) {
    suspend fun function1() {
        chanX.send(1)
        chanY.receive()
    }
}

class ClassB(private val chanZ: Channel<Int>, private val chanW: Channel<Int>) {
    suspend fun function2() {
        chanZ.send(2)
        chanW.receive()
    }
}

fun function3(chanY: Channel<Int>, chanW: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        chanY.send(3)
        chanW.receive()
    }
}

fun function4(chanX: Channel<Int>, chanZ: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        chanX.receive()
        chanZ.send(4)
    }
}

fun function5(chanY: Channel<Int>, chanZ: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        chanY.receive()
        chanZ.send(5)
    }
}

fun main(): Unit= runBlocking(pool) {
    val chanX = Channel<Int>()
    val chanY = Channel<Int>()
    val chanZ = Channel<Int>()
    val chanW = Channel<Int>()

    val a = ClassA(chanX, chanY)
    val b = ClassB(chanZ, chanW)

    launch(pool) { a.function1() }
    launch(pool) { b.function2() }
    launch(pool) { function3(chanY, chanW) }
    launch(pool) { function4(chanX, chanZ) }
    launch(pool) { function5(chanY, chanZ) }
}

class RunChecker733: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}