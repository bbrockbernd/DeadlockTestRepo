/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 6 different channels
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
package org.example.generated.test287
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
        }
        output.close()
    }
}

class Consumer1(val input: Channel<Int>, val output: Channel<String>) {
    suspend fun consumeAndForward() {
        for (i in input) {
            output.send("Cons1: $i")
        }
        output.close()
    }
}

class Consumer2(val input: Channel<String>, val output: Channel<String>) {
    suspend fun consumeAndForward() {
        for (i in input) {
            output.send("Cons2: $i")
        }
        output.close()
    }
}

fun producerJob(channel: Channel<Int>) = GlobalScope.launch {
    val producer = Producer(channel)
    producer.produce()
}

fun consumer1Job(input: Channel<Int>, output: Channel<String>) = GlobalScope.launch {
    val consumer1 = Consumer1(input, output)
    consumer1.consumeAndForward()
}

fun consumer2Job(input: Channel<String>, output: Channel<String>) = GlobalScope.launch {
    val consumer2 = Consumer2(input, output)
    consumer2.consumeAndForward()
}

fun printerJob(input: Channel<String>) = GlobalScope.launch {
    for (i in input) {
        println(i)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<String>(5)
    val channel3 = Channel<String>(5)

    producerJob(channel1)
    consumer1Job(channel1, channel2)
    consumer2Job(channel2, channel3)
    printerJob(channel3)

    delay(1000L) // Wait for coroutine execution
}