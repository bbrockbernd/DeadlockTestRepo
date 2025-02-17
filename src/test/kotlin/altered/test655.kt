/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.altered.test655
import org.example.altered.test655.RunChecker655.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassOne {
    suspend fun functionOne(channel: Channel<Int>) {
        val value = 1
        channel.send(value)
    }
}

class ClassTwo {
    suspend fun functionTwo(channel: Channel<Int>) {
        val value = channel.receive() * 2
        channel.send(value)
    }
}

class ClassThree {
    suspend fun functionThree(channel: Channel<Int>) {
        val value = channel.receive() * 2
        channel.send(value)
    }
}

fun functionFour(channel: Channel<Int>) {
    GlobalScope.launch(pool) {
        val value = channel.receive()
        println("Received: $value")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()
    val classOne = ClassOne()
    val classTwo = ClassTwo()
    val classThree = ClassThree()

    launch(pool) { classOne.functionOne(channel) }
    launch(pool) { classTwo.functionTwo(channel) }
    launch(pool) { classThree.functionThree(channel) }
    launch(pool) { functionFour(channel) }
    launch(pool) { functionFour(channel) }
}

class RunChecker655: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}