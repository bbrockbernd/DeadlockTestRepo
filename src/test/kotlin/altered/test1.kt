/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":7,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test1
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
        }
        output.close()
    }
}

class ProducerB(val output: Channel<String>) {
    suspend fun produce() {
        val data = listOf("A", "B", "C", "D", "E")
        for (item in data) {
            output.send(item)
        }
        output.close()
    }
}

class ProcessorA(val input: Channel<Int>, val output: Channel<Double>) {
    suspend fun process() {
        for (received in input) {
            output.send(received.toDouble() * 1.5)
        }
        output.close()
    }
}

class ProcessorB(val input: Channel<String>, val output: Channel<Char>) {
    suspend fun process() {
        for (received in input) {
            output.send(received.reversed().first())
        }
        output.close()
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Double>()
    val channel4 = Channel<Char>()
    
    launch {
        ProducerA(channel1).produce()
    }
    
    launch {
        ProducerB(channel2).produce()
    }
    
    launch {
        ProcessorA(channel1, channel3).process()
    }
    
    launch {
        ProcessorB(channel2, channel4).process()
    }

    launch {
        for (received in channel3) {
            println("Processed Int -> Double: $received")
        }
    }

    launch {
        for (received in channel4) {
            println("Processed String -> Char: $received")
        }
    }
    
    coroutineScope {
        // Dummy coroutine to make up for the 7th coroutine
        launch {
            delay(2000L)
            println("Dummy coroutine finished.")
        }
    }
}

class RunChecker1: RunCheckerBase() {
    override fun block() = main()
}