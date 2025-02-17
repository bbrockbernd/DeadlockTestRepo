/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test540
import org.example.altered.test540.RunChecker540.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MyChannelHandler(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }

    suspend fun processData() {
        val data = receiveData()
        println("Processed: $data")
    }
}

fun firstFunction(handler: MyChannelHandler) = runBlocking(pool) {
    launch(pool) {
        handler.sendData(1)
    }
}

fun secondFunction(handler: MyChannelHandler) = runBlocking(pool) {
    launch(pool) {
        handler.sendData(2)
    }
}

fun thirdFunction(handler: MyChannelHandler) = runBlocking(pool) {
    launch(pool) {
        handler.receiveData()
    }
}

fun fourthFunction(handler: MyChannelHandler) = runBlocking(pool) {
    launch(pool) {
        handler.processData()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>(3)
    val myHandler = MyChannelHandler(channel)

    firstFunction(myHandler)
    secondFunction(myHandler)
    thirdFunction(myHandler)
    fourthFunction(myHandler)
    
    delay(1000L)  // Give some time for coroutines to complete
}

class RunChecker540: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}