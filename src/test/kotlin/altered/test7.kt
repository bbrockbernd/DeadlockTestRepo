/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 8 different coroutines
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
package org.example.altered.test7
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelProvider {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>()
}

class CoroutineHandler(private val provider: ChannelProvider) {
    suspend fun process1() {
        provider.channel1.send(1)
        provider.channel2.receive()
    }

    suspend fun process2() {
        provider.channel2.send(2)
        provider.channel3.receive()
    }

    suspend fun process3() {
        provider.channel3.send(3)
        provider.channel1.receive()
    }
}

fun mainFunction1(handler: CoroutineHandler) = runBlocking {
    val job1 = launch { handler.process1() }
    val job2 = launch { handler.process2() }
    val job3 = launch { handler.process3() }
    Unit // Placeholder to avoid any unused variables warning
}

fun mainFunction2(handler: CoroutineHandler) = runBlocking {
    val job4 = launch { handler.process1() }
    val job5 = launch { handler.process2() }
    val job6 = launch { handler.process3() }
    Unit // Placeholder to avoid any unused variables warning
}

fun mainFunction3(handler: CoroutineHandler) = runBlocking {
    launch { handler.process1() }
    launch { handler.process2() }
    launch { handler.process3() }
}

fun main(): Unit{
    val provider = ChannelProvider()
    val handler = CoroutineHandler(provider)
    mainFunction1(handler)
    mainFunction2(handler)
    mainFunction3(handler)
}

class RunChecker7: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}