/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 5 different coroutines
- 2 different classes

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
package org.example.altered.test881
import org.example.altered.test881.RunChecker881.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class ClassB {
    val channel3 = Channel<Int>()
}

fun function1(a: ClassA) = runBlocking(pool) {
    launch(pool) {
        val value = a.channel1.receive()
        a.channel2.send(value)
    }
}

fun function2(a: ClassA) = runBlocking(pool) {
    launch(pool) {
        val value = a.channel2.receive()
        a.channel1.send(value)
    }
}

fun function3(b: ClassB) = runBlocking(pool) {
    launch(pool) {
        val value = b.channel3.receive()
        b.channel3.send(value)
    }
}

fun function4(a: ClassA, b: ClassB) = runBlocking(pool) {
    launch(pool) {
        a.channel1.send(1)
        val value = a.channel2.receive()
        b.channel3.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val a = ClassA()
    val b = ClassB()

    launch(pool) { function1(a) }
    launch(pool) { function2(a) }
    launch(pool) { function3(b) }
    launch(pool) { function4(a, b) }

    delay(500) // Just to prevent the main function from ending immediately
}

class RunChecker881: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}