/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":7,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 7 different coroutines
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
package org.example.altered.test390
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val output: SendChannel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
            delay(100)
        }
        output.close()
    }
}

class Consumer(private val input: ReceiveChannel<Int>, private val output: SendChannel<Int>) {
    suspend fun consume() {
        for (value in input) {
            output.send(value * 2)
            delay(100)
        }
        output.close()
    }
}

fun processPipeline(input: ReceiveChannel<Int>, output: SendChannel<String>) = GlobalScope.launch {
    for (value in input) {
        output.send("Value: $value")
        delay(50)
    }
    output.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(2)
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>(2)
    val channel4 = Channel<Int>(2)
    val channel5 = Channel<Int>(2)
    val channel6 = Channel<String>(2)

    val producer = Producer(channel1)
    val consumer1 = Consumer(channel1, channel2)
    val consumer2 = Consumer(channel2, channel3)
    val consumer3 = Consumer(channel3, channel4)

    launch { producer.produce() }
    launch { consumer1.consume() }
    launch { consumer2.consume() }
    launch { consumer3.consume() }
    launch { processPipeline(channel4, channel5) }
    launch { processPipeline(channel5, channel6) }

    for (result in channel6) {
        println(result)
    }
}

class RunChecker390: RunCheckerBase() {
    override fun block() = main()
}