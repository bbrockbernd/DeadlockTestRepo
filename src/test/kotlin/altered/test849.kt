/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 2 different coroutines
- 0 different classes

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
package org.example.altered.test849
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun functionA(channel1: SendChannel<Int>, n: Int) {
    val product = n * 2
    runBlocking {
        channel1.send(product)
    }
}

suspend fun functionB(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    for (i in 1..5) {
        val value = channel1.receive()
        channel2.send(value + 1)
    }
}

fun functionC(channel2: ReceiveChannel<Int>, channel3: SendChannel<String>) {
    runBlocking {
        for (i in 1..5) {
            val value = channel2.receive()
            channel3.send("Received: $value")
        }
    }
}

fun functionD(channel3: ReceiveChannel<String>) {
    runBlocking {
        for (i in 1..5) {
            println(channel3.receive())
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    
    launch {
        functionA(channel1, 5)
    }

    launch {
        functionB(channel1, channel2)
    }

    coroutineScope {
        functionC(channel2, channel3)
        functionD(channel3)
    }
}

class RunChecker849: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}