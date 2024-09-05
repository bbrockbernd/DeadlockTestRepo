/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":3,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.generated.test225
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Data

class Processor1 {
    fun process(channel1: Channel<Data>, channel2: Channel<Data>) {
        runBlocking {
            launch {
                val data = channel1.receive() // Awaiting data from channel1
                channel2.send(data) // Send the received data to channel2
            }
        }
    }
}

class Processor2 {
    fun process(channel2: Channel<Data>, channel3: Channel<Data>) {
        runBlocking {
            launch {
                val data = channel2.receive() // Awaiting data from channel2
                channel3.send(data) // Send the received data to channel3
            }
        }
    }
}

class Processor3 {
    fun process(channel3: Channel<Data>, channel1: Channel<Data>) {
        runBlocking {
            launch {
                val data = channel3.receive() // Awaiting data from channel3
                channel1.send(data) // Send the received data to channel1
            }
        }
    }
}

class DeadlockTest {
    fun testDeadlock() {
        val channel1 = Channel<Data>()
        val channel2 = Channel<Data>()
        val channel3 = Channel<Data>()

        val processor1 = Processor1()
        val processor2 = Processor2()
        val processor3 = Processor3()

        runBlocking {
            launch {
                processor1.process(channel1, channel2)
            }
            
            launch {
                processor2.process(channel2, channel3)
            }
            
            launch {
                processor3.process(channel3, channel1)
            }
        }
    }
}

fun main(): Unit{
    val deadlockTest = DeadlockTest()
    deadlockTest.testDeadlock()
}