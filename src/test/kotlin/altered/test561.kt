/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test561
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (input in inputChannel) {
            outputChannel.send(input * 2)
        }
    }
}

fun producer(channelA: Channel<Int>) {
    GlobalScope.launch {
        repeat(10) {
            channelA.send(it)
        }
        channelA.close()
    }
}

fun consumer(channelC: Channel<Int>) {
    GlobalScope.launch {
        for (item in channelC) {
            println("Consumed: $item")
        }
    }
}

fun middleMan(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
    GlobalScope.launch {
        for (item in channelA) {
            channelB.send(item + 1)
        }
        channelB.close()
        
        for (item in channelB) {
            channelC.send(item + 1)
        }
        channelC.close()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    val processor = Processor(channelA, channelB)
    
    launch {
        processor.process()
    }

    producer(channelA)
    consumer(channelC)
    middleMan(channelA, channelB, channelC)

    delay(1000)
}

class RunChecker561: RunCheckerBase() {
    override fun block() = main()
}