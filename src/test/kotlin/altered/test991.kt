/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test991
import org.example.altered.test991.RunChecker991.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<String>()
    val channel4 = Channel<String>(5)
    val channel5 = Channel<Boolean>()
}

fun provideData(handler: ChannelHandler) {
    runBlocking(pool) {
        launch(pool) { handler.channel1.send(1) }
        launch(pool) { handler.channel2.send(2) }
        launch(pool) { handler.channel3.send("A") }
        launch(pool) { handler.channel4.send("B") }
        launch(pool) { handler.channel5.send(true) }
    }
}

fun processData(handler: ChannelHandler): Int {
    var result = 0
    runBlocking(pool) {
        launch(pool) {
            result += handler.channel1.receive()
            result += handler.channel2.receive()
        }
        launch(pool) {
            handler.channel3.receive()
            handler.channel4.receive()
        }
        launch(pool) {
            handler.channel5.receive()
        }
    }
    return result
}

suspend fun handleChannels(handler: ChannelHandler): Int {
    var sum = 0
    coroutineScope {
        launch(pool) {
            sum += handler.channel1.receive()
            println("Received from channel1")
        }
        launch(pool) {
            sum += handler.channel2.receive()
            println("Received from channel2")
        }
        launch(pool) {
            handler.channel3.receive()
            handler.channel4.receive()
            println("Received Strings")
        }
        launch(pool) {
            handler.channel5.receive()
            println("Received Boolean")
        }
    }
    return sum
}

fun mainProcess(handler: ChannelHandler): Int {
    var sum = 0
    runBlocking(pool) {
        launch(pool) {
            sum += handleChannels(handler)
        }
    }
    return sum
}

fun main(): Unit{
    val handler = ChannelHandler()
    provideData(handler)
    val result1 = processData(handler)
    println("Result1: $result1")
    val result2 = mainProcess(handler)
    println("Result2: $result2")
}

class RunChecker991: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}