/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test448
import org.example.altered.test448.RunChecker448.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageProcessor(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    
    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }
    
    suspend fun sendToChannel2(value: String) {
        channel2.send(value)
    }
    
    suspend fun receiveFromChannel1() {
        val value = channel1.receive()
        println("Received from channel1: $value")
    }

    suspend fun receiveFromChannel2() {
        val value = channel2.receive()
        println("Received from channel2: $value")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val processor = MessageProcessor(channel1, channel2)
    
    launch(pool) {
        processor.sendToChannel1(42)
    }

    launch(pool) {
        processor.receiveFromChannel1()
    }

    launch(pool) {
        processor.sendToChannel2("Kotlin")
    }

    launch(pool) {
        processor.receiveFromChannel2()
    }
}

class RunChecker448: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}