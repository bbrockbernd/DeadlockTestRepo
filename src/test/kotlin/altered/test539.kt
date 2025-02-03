/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
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
package org.example.altered.test539
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>(1)

    suspend fun process() {
        for (i in 1..3) {
            channel.send(i)
        }
        channel.close()
    }
}

fun CoroutineScope.coro1(processor: Processor) = launch {
    repeat(3) {
        val received = processor.channel.receive()
        println("Coro1 received: $received")
    }
}

fun CoroutineScope.coro2(processor: Processor) = launch {
    repeat(3) {
        val received = processor.channel.receive()
        println("Coro2 received: $received")
    }
}

fun CoroutineScope.coro3(processor: Processor) = launch {
    processor.process()
}

fun main(): Unit{
    runBlocking {
        val processor = Processor()
        coro1(processor)
        coro2(processor)
        coro3(processor)
        launch {
            repeat(3) {
                val received = processor.channel.receive()
                println("Main received: $received")
            }
        }
    }
}

class RunChecker539: RunCheckerBase() {
    override fun block() = main()
}