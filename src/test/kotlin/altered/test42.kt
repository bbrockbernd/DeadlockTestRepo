/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 6 different coroutines
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
package org.example.altered.test42
import org.example.altered.test42.RunChecker42.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()

    fun produceData() = runBlocking(pool) {
        launch(pool) { ch1.send(1) }
        launch(pool) { ch2.send(2) }
    }

    fun consumeData() = runBlocking(pool) {
        launch(pool) { println(ch1.receive()) }
        launch(pool) { println(ch2.receive()) }
    }
}

class DataManager {
    val ch3 = Channel<String>(1)
    val ch4 = Channel<Double>(1)

    suspend fun processString() = coroutineScope {
        launch(pool) { ch3.send("Hello") }
    }

    suspend fun processDouble() = coroutineScope {
        launch(pool) { ch4.send(5.0) }
    }

    fun displayData() = runBlocking(pool) {
        launch(pool) { println(ch3.receive()) }
        launch(pool) { println(ch4.receive()) }
    }
}

class Coordinator {
    val ch5 = Channel<Boolean>()
    val ch6 = Channel<Char>()
    val ch7 = Channel<Long>()

    fun coordinateTasks() = runBlocking(pool) {
        launch(pool) { ch5.send(true) }
        launch(pool) { ch6.send('A') }
        launch(pool) { ch7.send(100L) }
    }

    fun completeTasks() = runBlocking(pool) {
        launch(pool) { println(ch5.receive()) }
        launch(pool) { println(ch6.receive()) }
        launch(pool) { println(ch7.receive()) }
    }
}

fun main(): Unit{
    val dataProcessor = DataProcessor()
    val dataManager = DataManager()
    val coordinator = Coordinator()

    dataProcessor.produceData()
    dataProcessor.consumeData()

    runBlocking(pool) {
        dataManager.processString()
        dataManager.processDouble()
    }
    dataManager.displayData()

    coordinator.coordinateTasks()
    coordinator.completeTasks()
}

class RunChecker42: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}