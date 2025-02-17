/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":7,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 5 different coroutines
- 4 different classes

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
package org.example.altered.test463
import org.example.altered.test463.RunChecker463.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    fun sendToChannel1(value: Int) = runBlocking(pool) { channel1.send(value) }
    fun receiveFromChannel2(): Int = runBlocking(pool) { channel2.receive() }
}

class B(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    fun sendToChannel3(value: Int) = runBlocking(pool) { channel3.send(value) }
    fun receiveFromChannel4(): Int = runBlocking(pool) { channel4.receive() }
}

class C(val channel5: Channel<Int>) {
    fun sendToChannel5(value: Int) = runBlocking(pool) { channel5.send(value) }
    fun receiveFromChannel5(): Int = runBlocking(pool) { channel5.receive() }
}

class D(val channel6: Channel<Int>, val channel7: Channel<Int>) {
    fun sendToChannel6(value: Int) = runBlocking(pool) { channel6.send(value) }
    fun receiveFromChannel7(): Int = runBlocking(pool) { channel7.receive() }
}

fun sendValuesToChannels(a: A, b: B, c: C, d: D) {
    a.sendToChannel1(1)
    b.sendToChannel3(2)
    c.sendToChannel5(3)
    d.sendToChannel6(4)
}

fun receiveValuesFromChannels(a: A, b: B, c: C, d: D): Int {
    val value1 = a.receiveFromChannel2()
    val value2 = b.receiveFromChannel4()
    val value3 = c.receiveFromChannel5()
    val value4 = d.receiveFromChannel7()
    return value1 + value2 + value3 + value4
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel3, channel4)
    val c = C(channel5)
    val d = D(channel6, channel7)

    launch(pool) { a.sendToChannel1(1) }
    launch(pool) { b.sendToChannel3(2) }
    launch(pool) { c.sendToChannel5(3) }
    launch(pool) { d.sendToChannel6(4) }
    
    launch(pool) {
        sendValuesToChannels(a, b, c, d)
        val result = receiveValuesFromChannels(a, b, c, d)
        println("Result: $result")
    }
}

class RunChecker463: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}