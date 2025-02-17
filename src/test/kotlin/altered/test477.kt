/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":8,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
- 8 different coroutines
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
package org.example.altered.test477
import org.example.altered.test477.RunChecker477.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel2, channel3) }
    launch(pool) { function3(channel3, channel1) }
    launch(pool) { function4(channel1) }
    launch(pool) { function5(channel2) }
    launch(pool) { function6(channel3) }
    launch(pool) { function7(channel1, channel2, channel3) }
    
    launch(pool) {
        delay(1000)
        println("Timeout: Potential Deadlock detected.")
        cancel()
    }
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.receive()
}

suspend fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    channel2.send(2)
    channel3.receive()
}

suspend fun function3(channel3: Channel<Int>, channel1: Channel<Int>) {
    channel3.send(3)
    channel1.receive()
}

suspend fun function4(channel1: Channel<Int>) {
    println(channel1.receive())
}

suspend fun function5(channel2: Channel<Int>) {
    println(channel2.receive())
}

suspend fun function6(channel3: Channel<Int>) {
    println(channel3.receive())
}

suspend fun function7(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    val sum = channel1.receive() + channel2.receive() + channel3.receive()
    println("Sum: $sum")
}

class RunChecker477: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}