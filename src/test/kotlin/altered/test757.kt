/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 2 different coroutines
- 1 different classes

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
package org.example.altered.test757
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Coordinator {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>(5)
    val channelC = Channel<Int>()
    val channelD = Channel<Int>(10)
}

fun produceA(coordinator: Coordinator) = CoroutineScope(Dispatchers.Default).launch {
    for (i in 1..5) {
        coordinator.channelA.send(i)
    }
}

fun consumeA(coordinator: Coordinator) = CoroutineScope(Dispatchers.Default).launch {
    repeat(5) {
        val value = coordinator.channelA.receive()
        coordinator.channelB.send(value * 2)
    }
}

suspend fun transferBToC(coordinator: Coordinator) {
    repeat(5) {
        val value = coordinator.channelB.receive()
        coordinator.channelC.send(value + 1)
    }
}

suspend fun transferCToD(coordinator: Coordinator) {
    repeat(5) {
        val value = coordinator.channelC.receive()
        coordinator.channelD.send(value - 1)
    }
}

fun consumeD(coordinator: Coordinator) = CoroutineScope(Dispatchers.Default).launch {
    repeat(5) {
        println(coordinator.channelD.receive())
    }
}

fun main(): Unit= runBlocking {
    val coordinator = Coordinator()
    
    produceA(coordinator)
    consumeA(coordinator)
    
    launch {
        transferBToC(coordinator)
    }
    
    launch {
        transferCToD(coordinator)
    }

    consumeD(coordinator)
}

class RunChecker757: RunCheckerBase() {
    override fun block() = main()
}