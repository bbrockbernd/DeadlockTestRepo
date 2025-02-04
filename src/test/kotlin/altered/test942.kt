/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test942
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender1 {
    val channel = Channel<Int>()
    suspend fun sendValues() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Sender2 {
    val channel = Channel<Int>()
    suspend fun sendValues() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

class Receiver {
    val channel = Channel<Int>()
    suspend fun receiveValues(fromChannel1: Channel<Int>, fromChannel2: Channel<Int>) {
        repeat(5) {
            val value1 = fromChannel1.receive()
            channel.send(value1)
        }
        repeat(5) {
            val value2 = fromChannel2.receive()
            channel.send(value2)
        }
    }
}

fun main(): Unit= runBlocking {
    val sender1 = Sender1()
    val sender2 = Sender2()
    val receiver = Receiver()
    
    launch {
        sender1.sendValues()
    }

    launch {
        sender2.sendValues()
    }

    launch {
        receiver.receiveValues(sender1.channel, sender2.channel)
    }

    launch {
        repeat(10) {
            val value = receiver.channel.receive()
            println(value)
        }
    }
}

class RunChecker942: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}