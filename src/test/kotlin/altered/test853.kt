/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test853
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Worker {
    suspend fun work(channel1: Channel<Int>, channel2: Channel<Int>) {
        val data = channel1.receive()
        channel2.send(data * 2)
    }
}

fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            val worker = Worker()
            worker.work(channel1, channel2)
        }
    }
}

fun functionTwo(channel: Channel<Int>) = runBlocking {
    channel.send(42)
}

fun functionThree(channel: Channel<Int>) = runBlocking {
    val result = channel.receive()
    println("Result: $result")
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        functionOne(channel1, channel2)
    }

    launch {
        functionTwo(channel1)
    }

    functionThree(channel2)
}

class RunChecker853: RunCheckerBase() {
    override fun block() = main()
}