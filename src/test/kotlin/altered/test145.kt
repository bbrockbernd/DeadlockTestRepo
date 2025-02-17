/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 5 different coroutines
- 5 different classes

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
package org.example.altered.test145
import org.example.altered.test145.RunChecker145.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerOne(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            println("ProducerOne sent: $i")
        }
    }
}

class ProducerTwo(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            channel.send(i)
            println("ProducerTwo sent: $i")
        }
    }
}

class ConsumerOne(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel.receive()
            println("ConsumerOne received: $value")
        }
    }
}

class ConsumerTwo(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel.receive()
            println("ConsumerTwo received: $value")
        }
    }
}

class ConsumerThree(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel.receive()
            println("ConsumerThree received: $value")
        }
    }
}

fun main(): Unit = runBlocking(pool) {
    val channel = Channel<Int>()
    
    launch(pool) {
        ProducerOne(channel).produce()
    }
    
    launch(pool) {
        ProducerTwo(channel).produce()
    }
    
    launch(pool) {
        ConsumerOne(channel).consume()
    }
    
    launch(pool) {
        ConsumerTwo(channel).consume()
    }
    
    launch(pool) {
        ConsumerThree(channel).consume()
    }
}

class RunChecker145: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}