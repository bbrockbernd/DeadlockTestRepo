/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.altered.test417
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>, val result: MutableList<Int>) {
    suspend fun consume() {
        for (element in channel) {
            result.add(element)
            delay(50)
        }
    }
}

class Processor(val input: MutableList<Int>, val output: MutableList<Int>) {
    suspend fun process() {
        for (element in input) {
            output.add(element * 2)
            delay(30)
        }
    }
}

class ResultCollector(val result: MutableList<Int>) {
    fun getResult(): List<Int> {
        return result.toList()
    }
}

class Display(val result: List<Int>) {
    fun displayResult() {
        for (value in result) {
            println(value)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val intermediateResult = mutableListOf<Int>()
    val finalResult = mutableListOf<Int>()

    val producer = Producer(channel)
    val consumer = Consumer(channel, intermediateResult)
    val processor = Processor(intermediateResult, finalResult)
    val resultCollector = ResultCollector(finalResult)
    val display = Display(resultCollector.getResult())

    launch { producer.produce() }
    launch { consumer.consume() }
    launch { processor.process() }
    launch { 
        delay(500)
        display.displayResult()
    }

    coroutineScope { delay(1000) }  // To keep the main coroutine alive for some time
}

class RunChecker417: RunCheckerBase() {
    override fun block() = main()
}