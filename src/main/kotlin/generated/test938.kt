/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.generated.test938
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun sender1() {
        for (i in 1..5) {
            delay(100)
            channel1.send(i)
        }
    }

    suspend fun sender2() {
        val messages = listOf("A", "B", "C", "D", "E")
        for (msg in messages) {
            delay(150)
            channel2.send(msg)
        }
    }
}

class SecondClass(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun receiver1() {
        repeat(5) {
            println("Receiver1 received: ${channel1.receive()}")
        }
    }

    suspend fun receiver2() {
        repeat(5) {
            println("Receiver2 received: ${channel2.receive()}")
        }
    }
}

class ThirdClass(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun mixedOps() {
        repeat(5) {
            println("MixedOps received (channel1): ${channel1.receive()}")
            delay(50)
            println("MixedOps received (channel2): ${channel2.receive()}")
        }
    }
}

fun runTest() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    val firstClass = FirstClass(channel1, channel2)
    val secondClass = SecondClass(channel1, channel2)
    val thirdClass = ThirdClass(channel1, channel2)

    launch { firstClass.sender1() }
    launch { firstClass.sender2() }
    launch { secondClass.receiver1() }
    launch { secondClass.receiver2() }
    launch { thirdClass.mixedOps() }
}

fun main(): Unit{
    runTest()
}