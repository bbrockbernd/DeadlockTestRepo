/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.altered.test974
import org.example.altered.test974.RunChecker974.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch(pool) {
        repeat(5) {
            val value = it * 2
            channel1.send(value)
        }
    }
    
    GlobalScope.launch(pool) {
        repeat(5) {
            val received = channel1.receive()
            channel2.send(received + 1)
        }
    }
}

fun functionTwo(channel2: Channel<Int>) = runBlocking(pool) {
    repeat(5) {
        val result = channel2.receive()
        println("Final result: $result")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    functionOne(channel1, channel2)
    functionTwo(channel2)
    
    delay(1000L) // Add some delay to let the coroutines complete
}

class RunChecker974: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}