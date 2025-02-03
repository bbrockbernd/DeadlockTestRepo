/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test514
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Producer2(private val channel: Channel<String>) {
    suspend fun produce() {
        for (i in listOf("a", "b", "c", "d", "e")) {
            channel.send(i)
        }
    }
}

fun process1(channel: Channel<Int>, otherChannel: Channel<String>) = runBlocking {
    coroutineScope {
        launch {
            for (i in 1..5) {
                val value = channel.receive()
                println("Processing Int: $value")
                otherChannel.send(value.toString())
            }
        }
    }
}

fun process2(channel: Channel<String>, otherChannel: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            for (i in 1..5) {
                val value = channel.receive()
                println("Processing String: $value")
                otherChannel.send(value.length)
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)

    runBlocking {
        launch { producer1.produce() }
        launch { producer2.produce() }
        process1(channel1, channel2)
        process2(channel2, channel1)
    }
}

class RunChecker514: RunCheckerBase() {
    override fun block() = main()
}