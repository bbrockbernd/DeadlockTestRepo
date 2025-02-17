/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.altered.test80
import org.example.altered.test80.RunChecker80.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Alpha(val input: Channel<Int>, val output: Channel<Int>)

class Bravo(val input: Channel<Int>, val output: Channel<Int>)

class Charlie(val input: Channel<Int>, val output: Channel<Int>)

class Delta(val input: Channel<Int>, val output: Channel<Int>)

class Echo(val input: Channel<Int>, val output: Channel<Int>)

fun alphaFunction(alpha: Alpha) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            alpha.input.send(i)
            val received = alpha.output.receive()
            println("Alpha received: $received")
        }
    }
}

fun bravoFunction(bravo: Bravo) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            val received = bravo.input.receive()
            bravo.output.send(received * 2)
            println("Bravo received and sent: $received")
        }
    }
}

fun charlieFunction(charlie: Charlie) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            val received = charlie.input.receive()
            charlie.output.send(received + 1)
            println("Charlie processed and sent: $received")
        }
    }
}

fun deltaFunction(delta: Delta) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            val received = delta.input.receive()
            delta.output.send(received - 1)
            println("Delta processed and sent: $received")
        }
    }
}

fun echoFunction(echo: Echo) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            val received = echo.input.receive()
            echo.output.send(received / 2)
            println("Echo processed and sent: $received")
        }
    }
}

fun initializeChannelsAndFunctions() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val alpha = Alpha(channel1, channel2)
    val bravo = Bravo(channel2, channel3)
    val charlie = Charlie(channel3, channel4)
    val delta = Delta(channel4, channel5)
    val echo = Echo(channel5, channel6)

    alphaFunction(alpha)
    bravoFunction(bravo)
    charlieFunction(charlie)
    deltaFunction(delta)
    echoFunction(echo)

    finalFunction(channel6, channel7)
}

fun finalFunction(input: Channel<Int>, finalOutput: Channel<Int>) {
    GlobalScope.launch(pool) {
        for (i in 1..5) {
            val received = input.receive()
            finalOutput.send(received + 10)
            println("Final processed and sent: $received")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    initializeChannelsAndFunctions()
    delay(2000)
}

class RunChecker80: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}