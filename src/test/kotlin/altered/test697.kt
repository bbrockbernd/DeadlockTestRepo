/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test697
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun funcA() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

class ClassB(private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun funcB() {
        val value = channel3.receive()
        channel4.send(value)
    }
}

class ClassC(
    private val channel1: Channel<Int>, 
    private val channel2: Channel<Int>, 
    private val channel3: Channel<Int>, 
    private val channel4: Channel<Int>
) {
    suspend fun funcC() {
        launch {
            channel1.send(10)
            val result = channel4.receive()
            println("Result: $result")
        }
    }
}

fun funcD(channel1: Channel<Int>, channel2: Channel<Int>): Job = GlobalScope.launch {
    val value = channel2.receive()
    channel1.send(value)
}

fun funcE(channel3: Channel<Int>, channel4: Channel<Int>): Job = GlobalScope.launch {
    val value = channel4.receive()
    channel3.send(value)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel1, channel2, channel3, channel4)

    launch { classA.funcA() }
    launch { classB.funcB() }
    classC.funcC()
    funcD(channel1, channel2)
    funcE(channel3, channel4)
}

class RunChecker697: RunCheckerBase() {
    override fun block() = main()
}