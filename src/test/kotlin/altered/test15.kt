/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":2,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 6 different channels
- 2 different coroutines
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
package org.example.altered.test15
import org.example.altered.test15.RunChecker15.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun createChannelA(): Channel<Int> = Channel()
fun createChannelB(): Channel<Int> = Channel(2)
fun createChannelC(): Channel<Int> = Channel(3)
fun createChannelD(): Channel<Int> = Channel()
fun createChannelE(): Channel<Int> = Channel(1)
fun createChannelF(): Channel<Int> = Channel(1)

suspend fun function1(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
    channelA.send(1)
    channelB.send(channelA.receive())
    channelC.send(channelB.receive())
}

suspend fun function2(channelD: Channel<Int>, channelE: Channel<Int>, channelF: Channel<Int>) {
    channelE.send(2)
    channelD.send(channelE.receive())
    channelF.send(channelD.receive())
}

fun main(): Unit= runBlocking(pool) {
    val channelA = createChannelA()
    val channelB = createChannelB()
    val channelC = createChannelC()
    val channelD = createChannelD()
    val channelE = createChannelE()
    val channelF = createChannelF()

    launch(pool) {
        function1(channelA, channelB, channelC)
    }

    launch(pool) {
        function2(channelD, channelE, channelF)
    }

    println(channelC.receive())
    println(channelF.receive())
}

class RunChecker15: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}