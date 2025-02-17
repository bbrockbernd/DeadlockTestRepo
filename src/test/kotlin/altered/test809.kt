/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test809
import org.example.altered.test809.RunChecker809.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender {
    val channel1 = Channel<Int>(2)
    val channel2 = Channel<Int>(2)

    suspend fun sendData() {
        channel1.send(1)
        channel2.send(2)
    }
}

class Receiver {
    val channel3 = Channel<Int>(2)
    val channel4 = Channel<Int>(2)

    suspend fun receiveData(sender: Sender) {
        val data1 = sender.channel1.receive()
        val data2 = sender.channel2.receive()
        channel3.send(data1)
        channel4.send(data2)
    }
}

fun processChannels(receiver: Receiver) {
    GlobalScope.launch(pool) {
        val data3 = receiver.channel3.receive()
        val data4 = receiver.channel4.receive()
        println("Processed data: $data3, $data4")
    }
}

fun main(): Unit= runBlocking(pool) {
    val sender = Sender()
    val receiver = Receiver()

    launch(pool) {
        sender.sendData()
    }

    launch(pool) {
        receiver.receiveData(sender)
    }

    processChannels(receiver)

    delay(1000L)
}

class RunChecker809: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}