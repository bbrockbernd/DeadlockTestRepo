/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":1,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test363
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>) {
    fun provideValue() {
        val value = 42
        channel1.offer(value)
    }
}

class ClassB(val channel2: Channel<String>) {
    fun transformValue() {
        val result = "Hello, World!"
        channel2.offer(result)
    }
}

class ClassC(val channel3: Channel<Boolean>) {
    fun checkCondition() {
        val condition = true
        channel3.offer(condition)
    }
}

class ClassD(
    val channel1: Channel<Int>,
    val channel3: Channel<Boolean>,
    val channel5: Channel<Int>
) {
    fun aggregateValues() {
        runBlocking {
            val value1 = channel1.receive()
            if (channel3.receive()) {
                val aggregated = value1 + 10
                channel5.offer(aggregated)
            }
        }
    }
}

fun func1(channel1: Channel<Int>, channel4: Channel<Long>) {
    runBlocking {
        val value = channel1.receive().toLong()
        channel4.offer(value)
    }
}

fun func2(channel2: Channel<String>, channel5: Channel<Int>) {
    runBlocking {
        val value = channel2.receive().length
        channel5.offer(value)
    }
}

fun func3(channel4: Channel<Long>) {
    runBlocking {
        channel4.receive()
        println("Received and processed a long value")
    }
}

fun func4(channel5: Channel<Int>) {
    runBlocking {
        val value = channel5.receive()
        println("Final value: $value")
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Boolean>()
    val channel4 = Channel<Long>()
    val channel5 = Channel<Int>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel3)
    val classD = ClassD(channel1, channel3, channel5)

    launch {
        classA.provideValue()
        func1(channel1, channel4)
    }
    
    launch {
        classB.transformValue()
        func2(channel2, channel5)
    }

    launch {
        classC.checkCondition()
        classD.aggregateValues()
    }

    launch {
        func3(channel4)
        func4(channel5)
    }
}

class RunChecker363: RunCheckerBase() {
    override fun block() = main()
}