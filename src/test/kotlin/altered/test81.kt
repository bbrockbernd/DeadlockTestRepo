/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 6 different coroutines
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
package org.example.altered.test81
import org.example.altered.test81.RunChecker81.Companion.pool
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
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    launch(pool) { function1(channel1, channel2) }
    launch(pool) { function2(channel2, channel3) }
    launch(pool) { function3(channel3, channel4) }
    launch(pool) { function4(channel5, channel4) }
    launch(pool) { function5(channel6, channel5) }
    launch(pool) { function6(channel7, channel8) }
    
    channel1.send(1)
    channel6.send(6)

    delay(1000) // Added delay to ensure that main doesn't exit before coroutines are done
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    val received = channel1.receive()
    println("Function 1 received: $received")
    channel2.send(received + 1)
}

suspend fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    val received = channel2.receive()
    println("Function 2 received: $received")
    channel3.send(received + 1)
}

suspend fun function3(channel3: Channel<Int>, channel4: Channel<Int>) {
    val received = channel3.receive()
    println("Function 3 received: $received")
    channel4.send(received + 1)
}

suspend fun function4(channel5: Channel<Int>, channel4: Channel<Int>) {
    val received = channel4.receive()
    println("Function 4 received: $received")
    channel5.send(received + 1)
}

suspend fun function5(channel6: Channel<Int>, channel5: Channel<Int>) {
    val received = channel6.receive()
    println("Function 5 received: $received")
    channel5.send(received + 1)
}

suspend fun function6(channel7: Channel<Int>, channel8: Channel<Int>) {
    val received = channel7.receive()
    println("Function 6 received: $received")
    channel8.send(received + 1)
    function7(channel8)
}

suspend fun function7(channel8: Channel<Int>) {
    val received = channel8.receive()
    println("Function 7 received: $received")
}

class RunChecker81: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}