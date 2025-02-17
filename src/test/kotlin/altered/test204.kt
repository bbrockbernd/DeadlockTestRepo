/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test204
import org.example.altered.test204.RunChecker204.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>) {
    suspend fun sendToChannel(value: Int) {
        channel.send(value)
    }
}

class ClassB(val channel: Channel<Int>) {
    suspend fun receiveFromChannel(): Int {
        return channel.receive()
    }
}

class ClassC(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun transferValue() {
        val value = channelA.receive()
        channelB.send(value)
    }
}

class ClassD(val channel: Channel<String>) {
    suspend fun sendString(value: String) {
        channel.send(value)
    }
}

class ClassE(val channel: Channel<String>) {
    suspend fun receiveString(): String {
        return channel.receive()
    }
}

fun function1(classA: ClassA) {
    runBlocking(pool) {
        launch(pool) {
            classA.sendToChannel(1)
        }
    }
}

fun function2(classB: ClassB): Int {
    var result = 0
    runBlocking(pool) {
        launch(pool) {
            result = classB.receiveFromChannel()
        }
    }
    return result
}

fun function3(classC: ClassC) {
    runBlocking(pool) {
        launch(pool) {
            classC.transferValue()
        }
    }
}

fun function4(classD: ClassD) {
    runBlocking(pool) {
        launch(pool) {
            classD.sendString("Hello")
        }
    }
}

fun function5(classE: ClassE): String {
    var result = ""
    runBlocking(pool) {
        launch(pool) {
            result = classE.receiveString()
        }
    }
    return result
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<String>()

    val classA = ClassA(channelA)
    val classB = ClassB(channelA)
    val classC = ClassC(channelA, channelB)
    val classD = ClassD(channelC)
    val classE = ClassE(channelC)

    function1(classA)
    val result1 = function2(classB)
    function3(classC)
    function4(classD)
    val result2 = function5(classE)

    println("Result from channel A: $result1")
    println("Result from channel C: $result2")
}

class RunChecker204: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}