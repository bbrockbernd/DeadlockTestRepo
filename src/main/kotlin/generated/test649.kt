/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.generated.test649
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (value in channel) {
            println("Consumed $value")
        }
    }
}

fun middleMan(channel1: Channel<Int>, channel2: Channel<Int>) = GlobalScope.launch {
    for (value in channel1) {
        println("Passing $value forward")
        channel2.send(value)
    }
    channel2.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val producer = Producer(channel1)
    val consumer = Consumer(channel2)

    val producerCoroutine = launch { producer.produce() }
    val middleManCoroutine = middleMan(channel1, channel2)
    val consumerCoroutine = launch { consumer.consume() }

    producerCoroutine.join()
    middleManCoroutine.join()
    consumerCoroutine.join()
}