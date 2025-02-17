/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test262
import org.example.altered.test262.RunChecker262.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Class1 {
    val channel = Channel<Int>()
}

class Class2 {
    val channel = Channel<Int>()
}

class Class3 {
    fun sendToChannel1(c1: Class1, value: Int) {
        GlobalScope.launch(pool) {
            c1.channel.send(value)
        }
    }
}

class Class4 {
    fun receiveFromChannel1(c1: Class1): Int = runBlocking(pool) {
        c1.channel.receive()
    }
}

class Class5 {
    fun sendAndReceive(c1: Class1, c2: Class2, value: Int) {
        GlobalScope.launch(pool) {
            c1.channel.send(value)
            c2.channel.receive()
        }
    }
}

fun sendValue(c3: Class3, c1: Class1, value: Int) {
    c3.sendToChannel1(c1, value)
}

suspend fun receiveValue(c4: Class4, c1: Class1): Int {
    return c4.receiveFromChannel1(c1)
}

fun main(): Unit= runBlocking(pool) {
    val c1 = Class1()
    val c2 = Class2()
    val c3 = Class3()
    val c4 = Class4()
    val c5 = Class5()

    sendValue(c3, c1, 10)
    println(receiveValue(c4, c1))

    launch(pool) {
        c5.sendAndReceive(c1, c2, 20)
    }
    launch(pool) {
        c1.channel.send(30)
        println(c2.channel.receive())
    }
}

class RunChecker262: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}