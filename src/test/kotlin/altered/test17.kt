/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 8 different channels
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
package org.example.altered.test17
import org.example.altered.test17.RunChecker17.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)

class ClassB(val channel: Channel<String>)

class ClassC(val channel1: Channel<Int>, val channel2: Channel<String>)

class ClassD(val channel1: Channel<String>, val channel2: Channel<Int>)

fun function1(a: ClassA) {
    runBlocking(pool) {
        repeat(5) {
            a.channel.send(it)
        }
    }
}

fun function2(b: ClassB) {
    runBlocking(pool) {
        repeat(5) {
            b.channel.send("Hello, $it")
        }
    }
}

fun function3(c: ClassC) {
    runBlocking(pool) {
        val value1 = c.channel1.receive()
        c.channel2.send("Value: $value1")
    }
}

fun function4(d: ClassD) {
    runBlocking(pool) {
        val value2 = d.channel1.receive()
        d.channel2.send(value2.length)
    }
}

fun function5(a: ClassA, b: ClassB, c: ClassC, d: ClassD) {
    runBlocking(pool) {
        launch(pool) { function1(a) }
        launch(pool) { function2(b) }
        launch(pool) { function3(c) }
        launch(pool) { function4(d) }
        launch(pool) {
            repeat(5) {
                d.channel1.send(b.channel.receive())
                c.channel1.send(a.channel.receive())
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<String>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<String>()

    val a = ClassA(channel1)
    val b = ClassB(channel2)
    val c = ClassC(channel3, channel4)
    val d = ClassD(channel6, channel5)
    
    function5(a, b, c, d)
}

class RunChecker17: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}