/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
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
package org.example.altered.test462
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun channelOne(c: SendChannel<Int>, r: ReceiveChannel<Int>) {
    GlobalScope.launch {
        for (x in 1..10) {
            c.send(x)
        }
    }
    GlobalScope.launch {
        for (x in r) {
            println("Received in channelOne: $x")
        }
    }
}

fun channelTwo(c: SendChannel<String>, r: ReceiveChannel<Int>) {
    GlobalScope.launch {
        for (x in r) {
            c.send("String $x")
        }
    }
}

suspend fun channelThree(c1: SendChannel<Double>, c2: SendChannel<Int>) {
    coroutineScope {
        launch {
            for (x in 1..10) {
                c1.send(x.toDouble())
            }
        }
        launch {
            for (x in 11..20) {
                c2.send(x)
            }
        }
    }
}

fun channelFour(c: SendChannel<Char>, r1: ReceiveChannel<String>, r2: ReceiveChannel<Double>) {
    GlobalScope.launch {
        for (x in r1) {
            c.send(x.first())
        }
    }
    GlobalScope.launch {
        for (x in r2) {
            c.send(x.toString().first())
        }
    }
}

suspend fun channelFive(c: SendChannel<Long>, r: ReceiveChannel<Int>) {
    coroutineScope {
        launch {
            for (x in r) {
                c.send(x.toLong())
            }
        }
    }
}

fun channelSix(c: SendChannel<Float>, r1: ReceiveChannel<Char>, r2: ReceiveChannel<Long>) {
    GlobalScope.launch {
        for (x in r1) {
            c.send(x.toFloat())
        }
    }
    GlobalScope.launch {
        for (x in r2) {
            c.send(x.toFloat())
        }
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<String>()
    val ch4 = Channel<Double>()
    val ch5 = Channel<Char>()
    val ch6 = Channel<Long>()
    val ch7 = Channel<Float>()

    channelOne(ch1, ch2)
    channelTwo(ch3, ch1)
    channelThree(ch4, ch2)
    channelFour(ch5, ch3, ch4)
    channelFive(ch6, ch2)
    channelSix(ch7, ch5, ch6)
    
    delay(10000) // wait for the coroutines to finish
}

class RunChecker462: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}