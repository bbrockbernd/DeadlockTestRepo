/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 2 different coroutines
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
package org.example.altered.test65
import org.example.altered.test65.RunChecker65.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Alpha(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToBeta(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveFromGamma(): Int {
        return channel2.receive()
    }
}

class Beta(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToGamma(value: Int) {
        channel2.send(value)
    }

    suspend fun receiveFromAlpha(): Int {
        return channel1.receive()
    }
}

class Gamma(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToDelta(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveFromBeta(): Int {
        return channel2.receive()
    }
}

class Delta(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToAlpha(value: Int) {
        channel2.send(value)
    }

    suspend fun receiveFromGamma(): Int {
        return channel1.receive()
    }
}

fun process(alpha: Alpha, beta: Beta, gamma: Gamma, delta: Delta) {
    runBlocking(pool) {
        val cor1 = launch(pool) {
            val aValue = 10
            alpha.sendToBeta(aValue)
            val dValue = delta.receiveFromGamma()
            println("Alpha received from Delta: $dValue")
        }

        val cor2 = launch(pool) {
            val bValue = beta.receiveFromAlpha()
            beta.sendToGamma(bValue * 2)
            val gValue = gamma.receiveFromBeta() * 3
            gamma.sendToDelta(gValue)
            println("Gamma sent to Delta: $gValue")
        }

        coroutineScope {
            cor1.join()
            cor2.join()
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    val alpha = Alpha(channel1, channel6)
    val beta = Beta(channel1, channel2)
    val gamma = Gamma(channel2, channel4)
    val delta = Delta(channel4, channel6)

    process(alpha, beta, gamma, delta)
}

class RunChecker65: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}