/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.altered.test265
import org.example.altered.test265.RunChecker265.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun sendValuesToB() {
        repeat(5) {
            delay(100)
            channelA.send(it)
        }
    }
    
    suspend fun receiveValuesFromA() {
        repeat(5) {
            val value = channelA.receive()
            channelB.send(value * 2)
        }
    }
}

class ClassB(private val channelB: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun sendValuesToC() {
        repeat(5) {
            val value = channelB.receive()
            channelC.send(value + 1)
        }
    }
    
    suspend fun receiveValuesFromB() {
        repeat(5) {
            val value = channelC.receive()
            println("ClassB received value: $value")
        }
    }
}

class ClassC(private val channelD: Channel<Int>) {
    suspend fun handleValuesFromD() {
        repeat(5) {
            val value = channelD.receive()
            println("ClassC received value: $value")
        }
    }
}

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) = ClassA(channelA, channelB)

fun function2(channelB: Channel<Int>, channelC: Channel<Int>) = ClassB(channelB, channelC)

fun function3(channelD: Channel<Int>) = ClassC(channelD)

fun function4(channelD: Channel<Int>) = runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    
    launch(pool) { function1(channelA, channelB).sendValuesToB() }
    launch(pool) { function1(channelA, channelB).receiveValuesFromA() }
    launch(pool) { function2(channelB, channelC).sendValuesToC() }
    
    launch(pool) { function2(channelB, channelC).receiveValuesFromB() }
    launch(pool) { function3(channelD).handleValuesFromD() }
    
    repeat(5) {
        delay(300)
        channelD.send(it + 10)
    }
}

fun main(): Unit{
    val channelD = Channel<Int>()
    function4(channelD)
}

class RunChecker265: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}