/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test778
import org.example.altered.test778.RunChecker778.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun sendData() {
        repeat(5) {
            delay(100)
            channel1.send(it)
            channel2.send(it * 2)
        }
    }

    suspend fun receiveData(): Int {
        val data1 = channel1.receive()
        val data2 = channel2.receive()
        return data1 + data2
    }
}

class SecondClass {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    suspend fun processData(input: Int) {
        channel3.send(input * 3)
        channel4.send(input * 4)
    }

    suspend fun fetchData(): Int {
        val data3 = channel3.receive()
        val data4 = channel4.receive()
        return data3 + data4
    }
}

fun main(): Unit= runBlocking(pool) {
    val firstClass = FirstClass()
    val secondClass = SecondClass()

    launch(pool) {
        firstClass.sendData()
    }

    launch(pool) {
        repeat(5) {
            val receivedData = firstClass.receiveData()
            secondClass.processData(receivedData)
        }
    }

    launch(pool) {
        repeat(5) {
            val result = secondClass.fetchData()
            println("Processed Data: $result")
        }
    }
}

class RunChecker778: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}