/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test535
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun startProducing() = runBlocking {
        coroutineScope {
            launch { produceToChannel1() }
            launch { produceToChannel2() }
        }
    }

    private suspend fun produceToChannel1() {
        for (i in 1..5) {
            channel1.send(i)
        }
        receiveFromChannel2()
    }

    private suspend fun produceToChannel2() {
        for (i in 6..10) {
            channel2.send(i)
        }
        receiveFromChannel1()
    }

    private suspend fun receiveFromChannel1() {
        for (i in 1..5) {
            println("Received from Channel1: ${channel1.receive()}")
        }
    }

    private suspend fun receiveFromChannel2() {
        for (i in 6..10) {
            println("Received from Channel2: ${channel2.receive()}")
        }
    }
}

class Consumer {
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    fun startConsuming() = runBlocking {
        coroutineScope {
            launch { consumeFromChannel3() }
            launch { consumeFromChannel4() }
        }
    }

    private suspend fun consumeFromChannel3() {
        sendToChannel4()
        for (i in 1..5) {
            println("Consumed from Channel3: ${channel3.receive()}")
        }
    }

    private suspend fun consumeFromChannel4() {
        sendToChannel3()
        for (i in 6..10) {
            println("Consumed from Channel4: ${channel4.receive()}")
        }
    }

    private suspend fun sendToChannel3() {
        for (i in 11..15) {
            channel3.send(i)
        }
    }

    private suspend fun sendToChannel4() {
        for (i in 16..20) {
            channel4.send(i)
        }
    }
}

fun main(): Unit{
    val producer = Producer()
    val consumer = Consumer()

    runBlocking {
        launch { producer.startProducing() }
        launch { consumer.startConsuming() }
    }
}

class RunChecker535: RunCheckerBase() {
    override fun block() = main()
}