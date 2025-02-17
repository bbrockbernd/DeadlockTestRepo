/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test638
import org.example.altered.test638.RunChecker638.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    private val channel1 = Channel<Int>(1)
    private val channel2 = Channel<Int>(1)
    private val channel3 = Channel<Int>(1)
    private val channel4 = Channel<Int>(1)

    suspend fun processChannels() {
        coroutineScope {
            launch(pool) {
                for (i in 1..3) {
                    channel1.send(i)
                    val received = channel2.receive()
                    println("Coroutine 1 received: $received")
                }
            }
            launch(pool) {
                for (i in 1..3) {
                    val received = channel1.receive()
                    channel2.send(received * 2)
                    println("Coroutine 2 received and sent: ${received * 2}")
                }
            }
            launch(pool) {
                for (i in 1..3) {
                    channel3.send(i * 3)
                    val received = channel4.receive()
                    println("Coroutine 3 received: $received")
                }
            }
            launch(pool) {
                for (i in 1..3) {
                    val received = channel3.receive()
                    channel4.send(received * 4)
                    println("Coroutine 4 received and sent: ${received * 4}")
                }
            }
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val handler = ChannelHandler()
    handler.processChannels()
}

class RunChecker638: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}