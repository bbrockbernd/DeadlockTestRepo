/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test670
import org.example.altered.test670.RunChecker670.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)
class B(val channel3: Channel<Int>)
class C(val channel4: Channel<Int>)

fun function1(a: A, b: B) = runBlocking(pool) {
    launch(pool) {
        val received1 = a.channel1.receive()
        b.channel3.send(received1)
        val received2 = a.channel2.receive()
        b.channel3.send(received2)
    }
    
    launch(pool) {
        val data = b.channel3.receive()
        a.channel2.send(data)
    }
}

fun function2(b: B, c: C) = runBlocking(pool) {
    launch(pool) {
        val data = b.channel3.receive()
        c.channel4.send(data)
    }
    
    launch(pool) {
        val received = c.channel4.receive()
        b.channel3.send(received)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>()
    
    val a = A(channel1, channel2)
    val b = B(channel3)
    val c = C(channel4)
    
    launch(pool) {
        function1(a, b)
    }
    
    launch(pool) {
        function2(b, c)
    }
    
    launch(pool) {
        a.channel1.send(42)
    }
}

class RunChecker670: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}