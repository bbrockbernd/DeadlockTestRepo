/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.altered.test785
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun produceNumbers(channel: SendChannel<Int>, range: IntRange) {
    CoroutineScope(Dispatchers.Default).launch {
        for (i in range) {
            channel.send(i)
        }
        channel.close()
    }
}

suspend fun filterEvenNumbers(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
    for (num in inputChannel) {
        if (num % 2 == 0) {
            outputChannel.send(num)
        }
    }
    outputChannel.close()
}

suspend fun squareNumbers(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
    for (num in inputChannel) {
        outputChannel.send(num * num)
    }
    outputChannel.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(5) // buffered channel

    produceNumbers(channel1, 1..10)

    launch {
        filterEvenNumbers(channel1, channel2)
    }

    launch {
        squareNumbers(channel2, channel3)
    }
    
    launch {
        for (result in channel3) {
            channel4.send(result)
        }
        channel4.close()
    }

    for (result in channel4) {
        println(result)
    }
}

class RunChecker785: RunCheckerBase() {
    override fun block() = main()
}