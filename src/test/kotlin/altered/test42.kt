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
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()

    fun produceData() = runBlocking {
        launch { ch1.send(1) }
        launch { ch2.send(2) }
    }

    fun consumeData() = runBlocking {
        launch { println(ch1.receive()) }
        launch { println(ch2.receive()) }
    }
}

class DataManager {
    val ch3 = Channel<String>(1)
    val ch4 = Channel<Double>(1)

    suspend fun processString() = coroutineScope {
        launch { ch3.send("Hello") }
    }

    suspend fun processDouble() = coroutineScope {
        launch { ch4.send(5.0) }
    }

    fun displayData() = runBlocking {
        launch { println(ch3.receive()) }
        launch { println(ch4.receive()) }
    }
}

class Coordinator {
    val ch5 = Channel<Boolean>()
    val ch6 = Channel<Char>()
    val ch7 = Channel<Long>()

    fun coordinateTasks() = runBlocking {
        launch { ch5.send(true) }
        launch { ch6.send('A') }
        launch { ch7.send(100L) }
    }

    fun completeTasks() = runBlocking {
        launch { println(ch5.receive()) }
        launch { println(ch6.receive()) }
        launch { println(ch7.receive()) }
    }
}

fun main(): Unit{
    val dataProcessor = DataProcessor()
    val dataManager = DataManager()
    val coordinator = Coordinator()

    dataProcessor.produceData()
    dataProcessor.consumeData()

    runBlocking {
        dataManager.processString()
        dataManager.processDouble()
    }
    dataManager.displayData()

    coordinator.coordinateTasks()
    coordinator.completeTasks()
}

class RunChecker42: RunCheckerBase() {
    override fun block() = main()
}