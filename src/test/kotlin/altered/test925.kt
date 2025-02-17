/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.altered.test925
import org.example.altered.test925.RunChecker925.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Example {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()
    val processingChannel1 = Channel<Int>()
    val processingChannel2 = Channel<Int>()
}

fun main(): Unit= runBlocking(pool) {
    val example = Example()

    launch(pool) {
        for (i in 1..5) {
            example.inputChannel.send(i)
        }
        example.inputChannel.close()
    }

    launch(pool) {
        processInput(example)
    }

    repeat(5) {
        println(example.outputChannel.receive())
    }
}

suspend fun processInput(example: Example) = coroutineScope {
    launch(pool) {
        for (i in example.inputChannel) {
            example.processingChannel1.send(i * 10)
        }
        example.processingChannel1.close()
    }

    launch(pool) {
        for (i in example.processingChannel1) {
            example.processingChannel2.send(i + 1)
        }
        example.processingChannel2.close()
    }

    launch(pool) {
        for (i in example.processingChannel2) {
            example.outputChannel.send(i * 2)
        }
        example.outputChannel.close()
    }
}

class RunChecker925: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}