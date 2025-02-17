/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 3 different coroutines
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
package org.example.altered.test281
import org.example.altered.test281.RunChecker281.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Channels
val channel1 = Channel<Int>(1)
val channel2 = Channel<Int>(1)
val channel3 = Channel<Int>(1)
val channel4 = Channel<Int>(1)
val channel5 = Channel<Int>(1)
val channel6 = Channel<Int>(1)

// Classes
class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)

// Functions
suspend fun function1(classA: ClassA) {
    val value = classA.channel.receive()
    channel1.send(value * 2)
}

suspend fun function2(classB: ClassB) {
    val value = classB.channel.receive()
    classB.channel.send(value + 1)
}

suspend fun function3(classC: ClassC) {
    val value = classC.channel.receive()
    classC.channel.send(value - 1)
}

fun function4() = runBlocking(pool) {
    launch(pool) {
        val classA = ClassA(channel2)
        function1(classA)
    }
    launch(pool) {
        val classB = ClassB(channel3)
        function2(classB)
    }
    launch(pool) {
        val classC = ClassC(channel4)
        function3(classC)
    }
}

fun function5() = runBlocking(pool) {
    channel2.send(10)
    println("Sent 10 to channel2")
    val result = channel1.receive()
    println("Received $result from channel1")
}

fun function6() = runBlocking(pool) {
    channel3.send(5)
    println("Sent 5 to channel3")
    val result = channel3.receive()
    println("Received $result from channel3")
}

fun main(): Unit{
    function4()
    function5()
    function6()
}

class RunChecker281: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}