/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 3 different coroutines
- 2 different classes

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
package org.example.altered.test500
import org.example.altered.test500.RunChecker500.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)

    suspend fun sendValues() {
        channel1.send(1)
        channel2.send(2)
    }
}

class Receiver {
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)

    suspend fun receiveValues() {
        println(channel3.receive())
        println(channel4.receive())
    }
}

suspend fun processChannels(sender: Sender, receiver: Receiver) {
    coroutineScope {
        launch(pool) {
            sender.channel1.send(3)
            receiver.channel3.send(sender.channel1.receive())
        }
        launch(pool) {
            sender.channel2.send(4)
            receiver.channel4.send(sender.channel2.receive())
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val sender = Sender()
    val receiver = Receiver()
    
    launch(pool) {
        sender.sendValues()
    }
    
    launch(pool) {
        processChannels(sender, receiver)
    }
    
    launch(pool) {
        receiver.receiveValues()
    }
}

class RunChecker500: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}