/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test896
import org.example.altered.test896.RunChecker896.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Message(val text: String)

fun produceMessages(channel: Channel<Message>) {
    GlobalScope.launch(pool) {
        val messages = listOf("Message 1", "Message 2", "Message 3")
        for (msg in messages) {
            channel.send(Message(msg))
        }
        channel.close()
    }
}

suspend fun consumeMessages(channel: Channel<Message>) {
    coroutineScope {
        for (msg in channel) {
            println("Received: ${msg.text}")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Message>()
    produceMessages(channel)
    consumeMessages(channel)
}

class RunChecker896: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}