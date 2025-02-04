/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test716
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel1: Channel<Int>) {
    suspend fun produce() {
        channel1.send(1)
    }
}

class Producer2(private val channel2: Channel<Int>) {
    suspend fun produce() {
        channel2.send(2)
    }
}

fun receiveFromChannel1(channel1: Channel<Int>) {
    runBlocking {
        println(channel1.receive())
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    
    runBlocking {
        val job = launch {
            producer1.produce()
            receiveFromChannel1(channel2)
        }
        producer2.produce()
        channel2.receive()
    }
}

class RunChecker716: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}