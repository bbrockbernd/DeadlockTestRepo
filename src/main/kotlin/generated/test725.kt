/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.generated.test725
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    private val channel1 = Channel<Int>(1)
    private val channel2 = Channel<Int>(1)

    suspend fun produce() {
        coroutineScope {
            launch {
                channel1.send(1)
                channel2.send(orReceive(channel1))
            }
        }
    }

    suspend fun orReceive(channel: Channel<Int>): Int {
        return channel.receive()
    }

    fun getChannel1(): Channel<Int> = channel1
    fun getChannel2(): Channel<Int> = channel2
}

class Consumer {
    private val channel3 = Channel<Int>(1)
    private val channel4 = Channel<Int>(1)
    private val channel5 = Channel<Int>()

    suspend fun consume() {
        coroutineScope {
            launch {
                val result1 = channel3.receive()
                channel4.send(result1 + 1)
            }
            launch {
                val result2 = channel4.receive()
                channel5.send(result2 + 1)
            }
            launch {
                channel5.receive()
                channel3.send(1)
            }
        }
    }

    fun getChannel3(): Channel<Int> = channel3
    fun getChannel4(): Channel<Int> = channel4
    fun getChannel5(): Channel<Int> = channel5
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    launch {
        consumer.getChannel3().send(producer.getChannel1().receive())
    }

    launch {
        consumer.getChannel4().send(producer.getChannel2().receive())
    }
}