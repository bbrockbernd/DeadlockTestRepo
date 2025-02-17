/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test921
import org.example.altered.test921.RunChecker921.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class Processor(val channel: Channel<Int>) {
    suspend fun processOne() {
        val value = channel.receive()
        println("Processed One: $value")
        processTwo(value)
    }
    
    suspend fun processTwo(value: Int) {
        println("Processed Two: $value")
        channel.send(value + 1)
    }
    
    suspend fun processThree() {
        delay(500)
        val value = 100
        channel.send(value)
    }
    
    suspend fun processFour() {
        val value = channel.receive()
        println("Processed Four: $value")
    }
    
    suspend fun processFive() {
        channel.send(200)
        val value = channel.receive()
        println("Processed Five: $value")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    val processor = Processor(channel)

    launch(pool) {
        processor.processOne()
    }

    launch(pool) {
        processor.processFive()
    }
}

class RunChecker921: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}