/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test588
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.coroutineScope

class Producer {
    private val channelA = Channel<Int>(10)

    fun produce() = channelA

    suspend fun startProducing() {
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }
}

class Consumer {
    private val channelB = Channel<String>(10)

    fun consume() = channelB

    suspend fun startConsuming(channelA: Channel<Int>) {
        for (i in 1..5) {
            val item = channelA.receive()
            channelB.send("Processed $item")
        }
        channelB.close()
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()

    launch {
        producer.startProducing()
    }

    launch {
        coroutineScope {
            consumer.startConsuming(producer.produce())
        }
    }

    launch {
        val channelB = consumer.consume()
        for (i in 1..5) {
            println(channelB.receive())
        }
    }
}

class RunChecker588: RunCheckerBase() {
    override fun block() = main()
}