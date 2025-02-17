/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 4 different coroutines
- 3 different classes

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
package org.example.altered.test695
import org.example.altered.test695.RunChecker695.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ResourceA(private val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..10) {
            channel.send(i)
        }
    }
}

class ResourceB(private val channel: Channel<Int>) {
    suspend fun consumeData(): Int {
        var sum = 0
        repeat(10) {
            sum += channel.receive()
        }
        return sum
    }
}

class ProcessorA(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun processData() {
        for (i in 1..10) {
            val received = inputChannel.receive()
            val processed = received * 2
            outputChannel.send(processed)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>(10)
    val channelB = Channel<Int>(10)
    val channelC = Channel<Int>(10)
    val channelD = Channel<Int>(10)

    val resourceA = ResourceA(channelA)
    val resourceB = ResourceB(channelD)
    val processorA = ProcessorA(channelA, channelB)
    
    launch(pool) {
        resourceA.produceData()
    }

    launch(pool) {
        processorA.processData()
    }

    launch(pool) {
        repeat(10) {
            val received = channelB.receive()
            channelC.send(received + 1)
        }
    }

    launch(pool) {
        var sum = 0
        repeat(10) {
            sum += channelC.receive()
        }
        channelD.send(sum)
    }

    val finalResult = resourceB.consumeData()
    println("Final result: $finalResult")
}

class RunChecker695: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}