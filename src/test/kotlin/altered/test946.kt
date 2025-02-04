/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test946
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class Consumer {
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
}

fun sendValues(producer: Producer) {
    runBlocking {
        launch {
            producer.channel1.send(1)
            producer.channel2.send(2)
        }
    }
}

suspend fun receiveAndForward(consumer: Consumer, producer: Producer) {
    coroutineScope {
        launch {
            val value1 = consumer.channel3.receive()
            producer.channel1.send(value1)
        }
        launch {
            val value2 = consumer.channel4.receive()
            producer.channel2.send(value2)
        }
    }
}

fun startProcess() {
    val producer = Producer()
    val consumer = Consumer()
    sendValues(producer)
    runBlocking {
        launch {
            receiveAndForward(consumer, producer)
        }
    }
}

fun main(): Unit{
    startProcess()
}

class RunChecker946: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}