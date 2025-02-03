/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test882
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// First class
class Producer(private val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(i * i)
        }
    }
}

// Second class
class Consumer(private val channel: Channel<Int>) {
    suspend fun consumeData() {
        for (i in 1..5) {
            val data = channel.receive()
            process(data)
        }
    }

    private fun process(data: Int) {
        println("Processed: $data")
    }
}

// Function 1: Initialize channels and launch coroutines
fun initiateChannelsAndCoroutines() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(3)
    
    runBlocking {
        launch { Producer(channel1).produceData() }
        launch { Consumer(channel1).consumeData() }
        launch { Sender(channel2).sendData() }
        launch { Receiver(channel2).receiveData() }
    }
}

// Function 2: Start the application
fun main(): Unit{
    initiateChannelsAndCoroutines()
}

// Function 3 (Used in second class): Additional coroutine for sender functionality
class Sender(private val channel: Channel<Int>) {
    suspend fun sendData() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

// Function 4 (Used in second class): Additional coroutine for receiver functionality
class Receiver(private val channel: Channel<Int>) {
    suspend fun receiveData() {
        for (i in 6..10) {
            val data = channel.receive()
            display(data)
        }
    }

    private fun display(data: Int) {
        println("Received: $data")
    }
}

// Function 5: Additional function to help with data flow
suspend fun handleData(channel: Channel<Int>) {
    val data = channel.receive()
    println("Handled data: $data")
}

class RunChecker882: RunCheckerBase() {
    override fun block() = main()
}