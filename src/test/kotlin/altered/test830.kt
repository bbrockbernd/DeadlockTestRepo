/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.altered.test830
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

class Producer {
    val channel = Channel<Int>()

    suspend fun produce() {
        channel.send(1)
        channel.send(2)
    }
}

class ConsumerA(val producer: Producer) {
    suspend fun consume() {
        val value1 = producer.channel.receive()
        producer.channel.send(value1 + 1)
    }
}

class ConsumerB(val producer: Producer) {
    suspend fun consume() {
        val value2 = producer.channel.receive()
        producer.channel.send(value2 + 1)
    }
}

fun runTest() = runBlocking {
    val producer = Producer()
    val consumerA = ConsumerA(producer)
    val consumerB = ConsumerB(producer)

    launch { producer.produce() }
    launch { consumerA.consume() }
    launch { consumerB.consume() }
    launch { producer.channel.receive() }
    launch { producer.channel.send(3) }
}

runTest()

class RunChecker830: RunCheckerBase() {
    override fun block() = main()
}