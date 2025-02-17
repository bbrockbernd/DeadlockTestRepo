/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.altered.test617
import org.example.altered.test617.RunChecker617.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA1: Channel<Int>, val channelA2: Channel<Int>) {
    suspend fun sendToChannels(value: Int) {
        channelA1.send(value)
        channelA2.send(value)
    }
}

class ClassB(val channelB1: Channel<Int>, val channelB2: Channel<Int>) {
    suspend fun receiveAndSend() {
        val received = channelB1.receive()
        channelB2.send(received)
    }
}

class ClassC(val channelC1: Channel<Int>, val channelC2: Channel<Int>) {
    suspend fun receiveAndPrint() {
        val received1 = channelC1.receive()
        val received2 = channelC2.receive()
        println("Received values: $received1 and $received2")
    }
}

suspend fun function1(classA: ClassA, value: Int) {
    classA.sendToChannels(value)
}

suspend fun function2(classB: ClassB, classC: ClassC) {
    classB.receiveAndSend()
    classC.receiveAndPrint()
}

fun main(): Unit= runBlocking(pool) {
    val channelA1 = Channel<Int>()
    val channelA2 = Channel<Int>()
    val channelB1 = Channel<Int>()
    val channelB2 = Channel<Int>()

    val classA = ClassA(channelA1, channelA2)
    val classB = ClassB(channelA1, channelB2)
    val classC = ClassC(channelA2, channelB2)

    launch(pool) {
        function1(classA, 42)
    }

    launch(pool) {
        function2(classB, classC)
    }
}

class RunChecker617: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}