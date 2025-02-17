/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.altered.test121
import org.example.altered.test121.RunChecker121.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    fun func1() = runBlocking(pool) {
        launch(pool) { coroutine1() }
        launch(pool) { coroutine2() }
        launch(pool) { coroutine3() }
        launch(pool) { coroutine4() }
        launch(pool) { coroutine5() }
        launch(pool) { coroutine6() }
        launch(pool) { coroutine7() }
    }

    suspend fun coroutine1() {
        val value = channel1.receive()
        channel2.send(value)
    }

    suspend fun coroutine2() {
        val value = channel2.receive()
        channel3.send(value)
    }

    suspend fun coroutine3() {
        val value = channel3.receive()
        channel1.send(value)
    }

    suspend fun coroutine4() {
        val channelValue = receiveFromChannel1()
        sendToChannel2(channelValue)
    }

    private suspend fun receiveFromChannel1(): Int {
        return channel1.receive()
    }

    private suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }

    suspend fun coroutine5() {
        val channelValue = receiveFromChannel2()
        sendToChannel3(channelValue)
    }

    private suspend fun receiveFromChannel2(): Int {
        return channel2.receive()
    }

    private suspend fun sendToChannel3(value: Int) {
        channel3.send(value)
    }

    suspend fun coroutine6() {
        channel1.send(1)
    }

    suspend fun coroutine7() {
        val channelValue = receiveFromChannel3()
        sendToChannel1(channelValue)
    }

    private suspend fun receiveFromChannel3(): Int {
        return channel3.receive()
    }

    private suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }
}

fun main(): Unit{
    val example = DeadlockExample()
    example.func1()
}

class RunChecker121: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}