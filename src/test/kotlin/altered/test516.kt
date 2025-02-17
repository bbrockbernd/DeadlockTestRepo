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
package org.example.altered.test516
import org.example.altered.test516.RunChecker516.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendToChannel1(value: Int) {
        ch1.send(value)
    }

    suspend fun receiveFromChannel2(): Int {
        return ch2.receive()
    }
}

class ClassB(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun sendToChannel3(value: Int) {
        ch3.send(value)
    }

    suspend fun receiveFromChannel4(): Int {
        return ch4.receive()
    }
}

class Processor(val classA: ClassA, val classB: ClassB) {
    suspend fun process() {
        val valueFromChannel1 = classA.receiveFromChannel2()
        classB.sendToChannel3(valueFromChannel1 + 1)
        val valueFromChannel3 = classB.receiveFromChannel4()
        classA.sendToChannel1(valueFromChannel3 * 2)
    }
}

fun functionOne(ch1: Channel<Int>, ch2: Channel<Int>) = ClassA(ch1, ch2)

fun functionTwo(ch3: Channel<Int>, ch4: Channel<Int>) = ClassB(ch3, ch4)

fun main(): Unit= runBlocking(pool) {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()

    val classA = functionOne(ch1, ch2)
    val classB = functionTwo(ch3, ch4)

    val processor = Processor(classA, classB)

    launch(pool) {
        processor.process()
    }

    launch(pool) {
        ch2.send(10)
        val result = ch1.receive()
        println("Result: $result")
        ch4.send(result + 5)
        val finalResult = ch3.receive()
        println("Final Result: $finalResult")
    }
}

class RunChecker516: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}