/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.altered.test948
import org.example.altered.test948.RunChecker948.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    suspend fun processChannels() {
        val receivedA = channelA.receive()
        channelB.send(receivedA)
        val receivedB = channelB.receive()
        channelC.send(receivedB)
        val receivedC = channelC.receive()
        channelA.send(receivedC)
    }
}

fun main(): Unit= runBlocking(pool) {
    val example = DeadlockExample()
    
    launch(pool) {
        example.channelA.send(1)
        val receivedFromC = example.channelC.receive()
        println("Received from Channel C: $receivedFromC")
    }

    launch(pool) {
        example.processChannels()
    }

    launch(pool) {
        val receivedFromB = example.channelB.receive()
        println("Received from Channel B: $receivedFromB")
    }
    
    delay(1000)
}

class RunChecker948: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}