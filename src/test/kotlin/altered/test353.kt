/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 6 different channels
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
package org.example.altered.test353
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>(1)
    private val channel3 = Channel<String>()
    private val channel4 = Channel<String>(1)
    private val channel5 = Channel<Double>(1)
    private val channel6 = Channel<Double>(1)

    suspend fun processIntegers() {
        val data = receiveInts()
        for (i in 1..5) {
            channel1.send(i)
        }
        repeat(5) {
            channel2.send(data[it])
        }
    }

    suspend fun receiveInts(): List<Int> {
        val list = mutableListOf<Int>()
        for (i in 1..5) {
            list.add(channel1.receive())
        }
        return list
    }

    suspend fun processStrings() {
        val data = receiveStrings()
        val results = data.map { it.reversed() }
        for (result in results) {
            channel4.send(result)
        }
    }

    suspend fun receiveStrings(): List<String> {
        val list = mutableListOf<String>()
        launch {
            channel3.send("Hello")
            channel3.send("World")
            channel3.send("from")
            channel3.send("Coroutines")
            channel3.send("!")
        }
        for (i in 1..5) {
            list.add(channel3.receive())
        }
        return list
    }

    fun startProcessing() = runBlocking {
        launch { processIntegers() }
        launch { receiveInts() }
        launch { processStrings() }
        launch { receiveStrings() }
        launch {
            delay(1000L)
            channel5.send(3.14)
            channel6.send(channel5.receive() * 2)
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcessing()
}

class RunChecker353: RunCheckerBase() {
    override fun block() = main()
}