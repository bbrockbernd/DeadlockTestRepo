/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.generated.test605
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>, val processChannel: Channel<Int>) {
    suspend fun consume() {
        for (value in channel) {
            processChannel.send(value * value)
        }
        processChannel.close()
    }
}

fun mainFunction() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producerA = Producer(channel1)
    val producerB = Producer(channel3)
    
    val consumerA = Consumer(channel1, channel2)
    val consumerB = Consumer(channel3, channel4)
    
    launch { producerA.produce() }
    launch { consumerA.consume() }
    launch { producerB.produce() }
    launch { consumerB.consume() }
    
    launch {
        for (result in channel4) {
            println("Processed value: $result")
        }
    }
}

mainFunction()