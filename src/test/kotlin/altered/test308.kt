/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":3,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test308
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor1 {
    val c1 = Channel<Int>()
    val c2 = Channel<Int>()

    suspend fun produce() {
        c1.send(1)
        println("Processor1 produced")
    }

    suspend fun consume() {
        val result = c1.receive()
        println("Processor1 consumed: $result")
    }
}

class Processor2 {
    val c3 = Channel<Int>()
    val c4 = Channel<Int>()

    suspend fun produce() {
        c3.send(2)
        println("Processor2 produced")
    }

    suspend fun consume() {
        val result = c3.receive()
        println("Processor2 consumed: $result")
    }
}

fun bridge1(p1: Processor1) {
    runBlocking {
        val job = launch {
            p1.produce()
            p1.consume()
        }
        job.join()
    }
}

fun bridge2(p2: Processor2) {
    runBlocking {
        val job = launch {
            p2.produce()
            p2.consume()
        }
    }
}

fun main(): Unit{
    val p1 = Processor1()
    val p2 = Processor2()

    runBlocking {
        val c5 = Channel<Int>()
        val c6 = Channel<Int>()
        val c7 = Channel<Int>()

        launch {
            bridge1(p1)
            c5.send(3)
            println("Sent to c5")
        }

        launch {
            bridge2(p2)
            c6.send(4)
            println("Sent to c6")
        }

        launch {
            bridge1(p1)
            c7.send(5)
            println("Sent to c7")
        }

        launch {
            c5.receive()
            println("Received from c5")
        }

        launch {
            c6.receive()
            println("Received from c6")
        }

        launch {
            c7.receive()
            println("Received from c7")
        }
    }
}

class RunChecker308: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}