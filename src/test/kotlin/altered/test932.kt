/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 2 different coroutines
- 3 different classes

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
package org.example.altered.test932
import org.example.altered.test932.RunChecker932.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Sender(private val channel: Channel<String>) {
    suspend fun sendMessages() {
        channel.send("Hello")
        channel.send("from")
        channel.send("Sender!")
    }
}

class Receiver(private val channel: Channel<String>) {
    suspend fun receiveMessages() {
        repeat(3) {
            val message = channel.receive()
            println(message)
        }
    }
}

class Tester {
    private val channelOne = Channel<String>()
    private val channelTwo = Channel<String>(2)

    suspend fun startTest() {
        coroutineScope {
            launch(pool) {
                val sender = Sender(channelOne)
                sender.sendMessages()
            }
            launch(pool) {
                val receiver = Receiver(channelTwo)
                receiver.receiveMessages()
            }
        }
    }

    fun execute() = runBlocking(pool) {
        channelTwo.send("Direct")
        channelTwo.send("Message")
        startTest()
    }
}

fun main(): Unit{
    val tester = Tester()
    tester.execute()
}

class RunChecker932: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}