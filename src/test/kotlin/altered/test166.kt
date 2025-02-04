/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":2,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test166
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker1(val channel: Channel<Int>) {
    suspend fun sendNumber(number: Int) {
        channel.send(number)
    }
}

class Worker2(val channel: Channel<Int>) {
    suspend fun receiveNumber(): Int {
        return channel.receive()
    }
}

class Processor1 {
    fun process(number: Int): Int {
        return number * 2
    }
}

class Processor2 {
    fun process(number: Int): Int {
        return number + 10
    }
}

class Aggregator {
    private var result = 0

    fun add(value: Int) {
        result += value
    }

    fun getResult(): Int {
        return result
    }
}

class Renderer {
    fun render(value: Int): String {
        return "Final Result: $value"
    }
}

fun createCoroutines(channel: Channel<Int>, aggregator: Aggregator, renderer: Renderer) {
    runBlocking {
        val job1 = launch {
            val worker1 = Worker1(channel)
            val processor1 = Processor1()
            worker1.sendNumber(processor1.process(5))
        }

        val job2 = launch {
            val worker2 = Worker2(channel)
            val processor2 = Processor2()
            val receivedNumber = worker2.receiveNumber()
            aggregator.add(processor2.process(receivedNumber))
            println(renderer.render(aggregator.getResult()))
        }

        job1.join()
        job2.join()
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val aggregator = Aggregator()
    val renderer = Renderer()
    createCoroutines(channel, aggregator, renderer)
}

class RunChecker166: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}