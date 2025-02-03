/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":6,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 6 different coroutines
- 5 different classes

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
package org.example.altered.test411
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager1 {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
}

class ChannelManager2 {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(5)
}

class ChannelManager3 {
    val channel5 = Channel<Int>()
}

class Processor1(val cm1: ChannelManager1) {
    suspend fun process() {
        repeat(100) {
            cm1.channel1.send(it)
            cm1.channel2.receive()
        }
    }
}

class Processor2(val cm2: ChannelManager2) {
    suspend fun process() {
        repeat(100) {
            cm2.channel3.send(it)
            cm2.channel4.receive()
        }
    }
}

fun runChannelManager1(cm1: ChannelManager1) = runBlocking {
    launch { Processor1(cm1).process() }
    launch {
        repeat(100) {
            cm1.channel2.send(it)
            cm1.channel1.receive()
        }
    }
}

fun runChannelManager2(cm2: ChannelManager2) = runBlocking {
    launch { Processor2(cm2).process() }
    launch {
        repeat(100) {
            cm2.channel4.send(it)
            cm2.channel3.receive()
        }
    }
}

fun feedChannelManager3(cm3: ChannelManager3) {
    runBlocking {
        launch {
            repeat(100) {
                cm3.channel5.send(it)
            }
        }
    }
}

fun runProcessors() {
    val cm1 = ChannelManager1()
    val cm2 = ChannelManager2()
    val cm3 = ChannelManager3()
    
    feedChannelManager3(cm3)
    runChannelManager1(cm1)
    runChannelManager2(cm2)
}

fun main(): Unit{
    runProcessors()
}

class RunChecker411: RunCheckerBase() {
    override fun block() = main()
}