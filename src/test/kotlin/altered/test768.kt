/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test768
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    fun produce(channel: Channel<Int>) {
        CoroutineScope(Dispatchers.Default).launch {
            (1..5).forEach {
                channel.send(it)
            }
            channel.close()
        }
    }
}

class ConsumerA {
    fun consume(channel: Channel<Int>, otherChannel: Channel<Int>) {
        CoroutineScope(Dispatchers.Default).launch {
            for (value in channel) {
                otherChannel.send(value * 2)
            }
            otherChannel.close()
        }
    }
}

class ConsumerB {
    fun consume(channel: Channel<Int>) {
        CoroutineScope(Dispatchers.Default).launch {
            for (value in channel) {
                println("Received: $value")
            }
        }
    }
}

suspend fun process() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = Producer()
    val consumerA = ConsumerA()
    val consumerB = ConsumerB()

    producer.produce(channel1)
    consumerA.consume(channel1, channel2)
    consumerB.consume(channel2)
}

fun main(): Unit= runBlocking {
    process()
    delay(1000L) // Give some time for all operations to complete
}

class RunChecker768: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}