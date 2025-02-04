/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test506
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExampleClass(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    fun simpleSend(value: Int) {
        runBlocking {
            channel1.send(value)
        }
    }

    suspend fun simpleReceive(): Int {
        return channel1.receive()
    }

    fun doubleSend(value1: Int, value2: Int) {
        runBlocking {
            launch { channel1.send(value1) }
            launch { channel2.send(value2) }
        }
    }

    suspend fun doubleReceive(): Pair<Int, Int> {
        val value1 = channel1.receive()
        val value2 = channel2.receive()
        return Pair(value1, value2)
    }

    suspend fun combinedOperation(value: Int): Int {
        channel1.send(value)
        val received = channel2.receive()
        return value + received
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val example = ExampleClass(channel1, channel2)

    runBlocking {
        launch {
            example.simpleSend(10)
        }
        launch {
            println(example.simpleReceive())
        }
        launch {
            val result = example.combinedOperation(5)
            println(result)
        }
        example.doubleSend(20, 30)
        val result = example.doubleReceive()
        println("Received: ${result.first} and ${result.second}")
    }
}

class RunChecker506: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}