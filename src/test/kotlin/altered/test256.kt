/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test256
import org.example.altered.test256.RunChecker256.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val id: Int, val channel1: Channel<String>, val channel2: Channel<String>, val channel3: Channel<String>)

fun processA(processor: Processor) = runBlocking(pool) {
    launch(pool) { 
        val data = processor.channel1.receive()
        processor.channel2.send("Processed in A: $data")
    }
}

fun processB(processor: Processor) = runBlocking(pool) {
    launch(pool) { 
        val data = processor.channel2.receive()
        processor.channel3.send("Processed in B: $data")
    }
}

fun processC(processor: Processor) = runBlocking(pool) {
    launch(pool) { 
        val data = processor.channel3.receive()
        processor.channel1.send("Processed in C: $data")
    }
}

fun initialSend(channel: Channel<String>, message: String) = runBlocking(pool) {
    launch(pool) {
        channel.send(message)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<String>(1)
    val channel2 = Channel<String>(1)
    val channel3 = Channel<String>(1)

    val processor = Processor(1, channel1, channel2, channel3)

    initialSend(channel1, "Start")
    
    launch(pool) { processA(processor) }
    launch(pool) { processB(processor) }
    launch(pool) { processC(processor) }
}

class RunChecker256: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}