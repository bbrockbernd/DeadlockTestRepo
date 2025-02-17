/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test838
import org.example.altered.test838.RunChecker838.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SampleClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>()
}

suspend fun function1(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.receive()
}

suspend fun function2(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.receive()
}

suspend fun function3(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.receive()
}

suspend fun function4(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.receive()
}

suspend fun function5(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.receive()
}

fun main(): Unit= runBlocking(pool) {
    val sample = SampleClass()
    
    launch(pool) {
        function1(sample.channel1, 1)
        function2(sample.channel2, 2)
        function3(sample.channel3, 3)
        function4(sample.channel4, 4)
        function5(sample.channel5, 5)
    }
    
    sample.channel1.send(10)
    sample.channel2.send(20)
    sample.channel3.send(30)
    sample.channel4.send(40)
    sample.channel5.send(50)
}

class RunChecker838: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}