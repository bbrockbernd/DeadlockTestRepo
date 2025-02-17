/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.altered.test503
import org.example.altered.test503.RunChecker503.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()

fun CoroutineScope.coroutine1() {
    launch(pool) {
        delay(100)
        channel1.send(1)
        println("coroutine1 sent 1")
        val received = channel2.receive()
        println("coroutine1 received $received")
    }
}

fun CoroutineScope.coroutine2() {
    launch(pool) {
        delay(200)
        val received = channel1.receive()
        println("coroutine2 received $received")
        channel3.send(2)
        println("coroutine2 sent 2")
    }
}

fun CoroutineScope.coroutine3() {
    launch(pool) {
        delay(300)
        val received = channel3.receive()
        println("coroutine3 received $received")
        channel4.send(3)
        println("coroutine3 sent 3")
    }
}

fun CoroutineScope.coroutine4() {
    launch(pool) {
        delay(400)
        val received = channel4.receive()
        println("coroutine4 received $received")
        channel5.send(4)
        println("coroutine4 sent 4")
    }
}

fun CoroutineScope.coroutine5() {
    launch(pool) {
        delay(500)
        val received = channel5.receive()
        println("coroutine5 received $received")
        channel2.send(5)
        println("coroutine5 sent 5")
    }
}

fun main(): Unit= runBlocking(pool) {
    coroutine1()
    coroutine2()
    coroutine3()
    coroutine4()
    coroutine5()

    delay(2000) // Give enough time for all coroutines to complete
}

class RunChecker503: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}