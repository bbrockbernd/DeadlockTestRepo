/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":6,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 6 different coroutines
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
package org.example.altered.test66
import org.example.altered.test66.RunChecker66.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    fun functionA(channel: Channel<Int>) = runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                println("ClassA:functionA - Sending data")
                channel.send(1)
            }
        }
    }
}

class ClassB {
    fun functionB(channel: Channel<Int>) = runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                println("ClassB:functionB - Receiving data")
                val data = channel.receive()
                println("ClassB:functionB - Received: $data")
            }
        }
    }
}

class ClassC {
    fun functionC(channel: Channel<Int>) = runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                println("ClassC:functionC - Sending data")
                channel.send(2)
            }
        }
    }
}

class ClassD {
    fun functionD(channel: Channel<Int>) = runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                println("ClassD:functionD - Receiving data")
                val data = channel.receive()
                println("ClassD:functionD - Received: $data")
            }
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()

    ClassA().functionA(channel)
    ClassB().functionB(channel)
    ClassC().functionC(channel)
    ClassD().functionD(channel)

    coroutineScope {
        launch(pool) { ClassA().functionA(channel) }
        launch(pool) { ClassB().functionB(channel) }
        launch(pool) { ClassC().functionC(channel) }
        launch(pool) { ClassD().functionD(channel) }
    }
}

class RunChecker66: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}