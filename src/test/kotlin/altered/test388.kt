/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test388
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        val coroutine1 = launch {
            val value = channel1.receive()
            channel2.send(value)
        }
        
        val coroutine2 = launch {
            val value = channel2.receive()
            channel1.send(value)
        }
        
        coroutine1.join()
        coroutine2.join()
    }
}

fun functionB(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channel2.receive()
        channel3.send(value)
    }
}

fun functionC(channel3: Channel<Int>, channel1: Channel<Int>) = runBlocking {
    launch {
        val value = channel3.receive()
        channel1.send(value)
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    functionA(channel1, channel2)
    functionB(channel2, channel3)
    functionC(channel3, channel1)
}

class RunChecker388: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}