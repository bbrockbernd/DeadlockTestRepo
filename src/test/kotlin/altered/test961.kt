/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.altered.test961
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassOne(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun funcOne() {
        channelB.send(1)
        val received = channelA.receive()
        println("ClassOne received: $received")
    }
}

class ClassTwo(val channelC: Channel<Int>, val channelD: Channel<Int>) {
    suspend fun funcTwo() {
        channelD.send(2)
        val received = channelC.receive()
        println("ClassTwo received: $received")
    }
}

suspend fun funcThree(channelA: Channel<Int>, channelC: Channel<Int>, channelD: Channel<Int>) {
    coroutineScope {
        val receivedA = async { channelA.receive() }
        channelD.send(receivedA.await())
        val receivedC = channelC.receive()
        println("funcThree received: $receivedC")
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val objOne = ClassOne(channelA, channelB)
    val objTwo = ClassTwo(channelC, channelD)

    launch { objOne.funcOne() }
    launch { objTwo.funcTwo() }
    launch { funcThree(channelA, channelC, channelD) }

    delay(1000) // Give some time before closing
    channelA.cancel()
    channelB.cancel()
    channelC.cancel()
    channelD.cancel()
}

class RunChecker961: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}