/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test591
import org.example.altered.test591.RunChecker591.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    launch(pool) {
        for (i in channel1) {
            channel2.send(i * 2)
        }
        channel2.close()
    }
}

fun function2(channel2: Channel<Int>, channel3: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        for (i in channel2) {
            channel3.send("Value: $i")
        }
        channel3.close()
    }
}

fun function3(channel3: Channel<String>, channel4: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        for (msg in channel3) {
            channel4.send(msg.reversed())
        }
        channel4.close()
    }
}

fun function4(channel4: Channel<String>, channel5: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        for (msg in channel4) {
            channel5.send("Processed: $msg")
        }
        channel5.close()
    }
}

fun function5(channel5: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        for (msg in channel5) {
            println(msg)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()
    
    function1(channel1, channel2)
    function2(channel2, channel3)
    function3(channel3, channel4)
    function4(channel4, channel5)
    function5(channel5)
}

class RunChecker591: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}