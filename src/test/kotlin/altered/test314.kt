/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 4 different coroutines
- 5 different classes

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
package org.example.altered.test314
import org.example.altered.test314.RunChecker314.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataA(val value: String)
class DataB(val number: Int)
class DataC(val isValid: Boolean)
class DataD(val id: Long)
class DataE(val name: String)

suspend fun sendDataA(channelA: Channel<DataA>) {
    channelA.send(DataA("Hello"))
}

suspend fun sendDataB(channelB: Channel<DataB>) {
    channelB.send(DataB(42))
}

suspend fun processAndSend(channelA: Channel<DataA>, channelB: Channel<DataB>, channelA2: Channel<DataA>) {
    val dataA = channelA.receive()
    val dataB = channelB.receive()
    channelA2.send(DataA(dataA.value + dataB.number))
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<DataA>()
    val channelB = Channel<DataB>()
    val channelA2 = Channel<DataA>()

    launch(pool) { sendDataA(channelA) }
    launch(pool) { sendDataB(channelB) }
    launch(pool) { processAndSend(channelA, channelB, channelA2) }
    launch(pool) { 
        val result = channelA2.receive()
        println("Processed result: ${result.value}") 
    }
}

class RunChecker314: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}