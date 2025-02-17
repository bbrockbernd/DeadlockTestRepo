/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
- 7 different coroutines
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
package org.example.altered.test191
import org.example.altered.test191.RunChecker191.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    
    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel2, channel3) }
    launch(pool) { function3(channel3, channel4) }
    launch(pool) { function4(channel4, channel5) }
    launch(pool) { function5(channel5, channel1) }
    launch(pool) { function6(channel1, channel2, channel3, channel4, channel5) }
    launch(pool) { extraCoroutine(channel5) }

    delay(1000L)
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    repeat(10) {
        channel1.send(it)
        val receive = channel2.receive()
        println("function1 received: $receive")
    }
}

suspend fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    repeat(10) {
        val receive = channel2.receive()
        println("function2 received: $receive")
        channel3.send(receive + 1)
    }
}

suspend fun function3(channel3: Channel<Int>, channel4: Channel<Int>) {
    repeat(10) {
        val receive = channel3.receive()
        println("function3 received: $receive")
        channel4.send(receive + 1)
    }
}

suspend fun function4(channel4: Channel<Int>, channel5: Channel<Int>) {
    repeat(10) {
        val receive = channel4.receive()
        println("function4 received: $receive")
        channel5.send(receive + 1)
    }
}

suspend fun function5(channel5: Channel<Int>, channel1: Channel<Int>) {
    repeat(10) {
        val receive = channel5.receive()
        println("function5 received: $receive")
        channel1.send(receive + 1)
    }
}

suspend fun function6(
    channel1: Channel<Int>,
    channel2: Channel<Int>,
    channel3: Channel<Int>,
    channel4: Channel<Int>,
    channel5: Channel<Int>
) {
    repeat(10) {
        val v1 = channel1.receive()
        val v2 = channel2.receive()
        val v3 = channel3.receive()
        val v4 = channel4.receive()
        val v5 = channel5.receive()
        
        println("function6 received values: $v1, $v2, $v3, $v4, $v5")
        
        channel1.send(v1 + v5)
        channel2.send(v2 + v1)
        channel3.send(v3 + v2)
        channel4.send(v4 + v3)
        channel5.send(v5 + v4)
    }
}

suspend fun extraCoroutine(channel5: Channel<Int>) {
    for (i in 0 until 10) {
        channel5.send(i)
        println("extraCoroutine sent: $i")
    }
}

class RunChecker191: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}