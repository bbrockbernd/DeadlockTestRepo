/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test197
import org.example.altered.test197.RunChecker197.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>(1)
val channel2 = Channel<Int>(2)
val channel3 = Channel<Int>(3)

fun main(): Unit= runBlocking(pool) {
    launch(pool) { coroutine1() }
    launch(pool) { coroutine2() }
    launch(pool) { coroutine3() }
    launch(pool) { coroutine4() }
    launch(pool) { coroutine5() }
}

suspend fun coroutine1() {
    sendToChannel1()
    receiveFromChannel2()
}

suspend fun coroutine2() {
    sendToChannel2()
    receiveFromChannel3()
}

suspend fun coroutine3() {
    sendToChannel3()
    receiveFromChannel1()
}

suspend fun coroutine4() {
    receiveFromChannel1()
    sendToChannel2()
}

suspend fun coroutine5() {
    receiveFromChannel2()
    sendToChannel3()
}

suspend fun sendToChannel1() {
    channel1.send(1)
}

suspend fun receiveFromChannel1() {
    channel1.receive()
}

suspend fun sendToChannel2() {
    channel2.send(2)
}

suspend fun receiveFromChannel2() {
    channel2.receive()
}

suspend fun sendToChannel3() {
    channel3.send(3)
}

suspend fun receiveFromChannel3() {
    channel3.receive()
}

class RunChecker197: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}