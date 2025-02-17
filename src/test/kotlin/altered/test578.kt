/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test578
import org.example.altered.test578.RunChecker578.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun provideData(channel1: Channel<Int>) = runBlocking(pool) {
    repeat(5) {
        channel1.send(it)
    }
    channel1.close()
}

suspend fun processA(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (element in channel1) {
        channel2.send(element * 2)
    }
    channel2.close()
}

fun consumeData(channel2: Channel<Int>) = runBlocking(pool) {
    for (element in channel2) {
        println("Consumed: $element")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<Int>()
    
    provideData(channel1)
    launch(pool) { processA(channel1, channel2) }
    consumeData(channel2)
}

class RunChecker578: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}