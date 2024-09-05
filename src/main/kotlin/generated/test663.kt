/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
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
package org.example.generated.test663
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<String>) {
    suspend fun produceInts() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    suspend fun produceStrings() {
        val words = listOf("one", "two", "three", "four", "five")
        for (word in words) {
            channel2.send(word)
        }
        channel2.close()
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<String>, val channel3: Channel<Pair<Int, String>>) {
    suspend fun consumeAndCombine() {
        for (int in channel1) {
            channel2.receiveOrNull()?.let {
                channel3.send(Pair(int, it))
            }
        }
        channel3.close()
    }
}

fun runProducers(producer: Producer, ch3: Channel<Pair<Int, String>>) {
    val scope = CoroutineScope(Dispatchers.Default)
    val ch4 = Channel<String>()
    val ch5 = Channel<Int>()

    scope.launch {
        producer.produceInts()
    }
    
    scope.launch {
        producer.produceStrings()
    }
    
    scope.launch {
        channelConsumer(ch3, ch4, ch5)
    }
}

suspend fun channelConsumer(channel3: Channel<Pair<Int, String>>, ch4: Channel<String>, ch5: Channel<Int>) {
    for (elem in channel3) {
        ch4.send(elem.second)
        ch5.send(elem.first)
    }
    ch4.close()
    ch5.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Pair<Int, String>>()
    
    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel1, channel2, channel3)
    
    launch {
        consumer.consumeAndCombine()
    }

    runProducers(producer, channel3)
}