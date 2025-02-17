/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test690
import org.example.altered.test690.RunChecker690.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.*
import kotlinx.coroutines.launch

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveFromChannel2(): Int {
        return channel2.receive()
    }
}

class ClassB(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }

    suspend fun receiveFromChannel1(): Int {
        return channel1.receive()
    }
}

class ClassC(val a: ClassA, val b: ClassB) {
    suspend fun process() {
        a.sendToChannel1(1)
        b.sendToChannel2(1)
        a.receiveFromChannel2()
        b.receiveFromChannel1()
    }
}

fun testDeadlock() = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val a = ClassA(channel1, channel2)
    val b = ClassB(channel1, channel2)
    val c = ClassC(a, b)

    val job = launch(pool) {
        c.process()
    }

    job.join()

}

fun main(): Unit{
    testDeadlock()
}

class RunChecker690: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}