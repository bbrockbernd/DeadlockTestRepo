/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":8,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 8 different coroutines
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
package org.example.generated.test298
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DataProducer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }
}

class DataProducer2(private val channel: Channel<String>) {
    suspend fun produce() {
        repeat(5) {
            channel.send("Data_$it")
        }
        channel.close()
    }
}

class DataConsumer(private val intChannel: Channel<Int>, private val stringChannel: Channel<String>) {
    suspend fun consume(): Pair<List<Int>, List<String>> {
        val ints = mutableListOf<Int>()
        val strings = mutableListOf<String>()
        for (element in intChannel) {
            ints.add(element)
        }
        for (element in stringChannel) {
            strings.add(element)
        }
        return Pair(ints, strings)
    }
}

fun function1(channel: Channel<Int>): ReceiveChannel<Int> = channel

fun function2(channel: Channel<String>): ReceiveChannel<String> = channel

fun function3(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in 10..15) {
            channel.send(i)
        }
        channel.close()
    }
}

fun function4(channel: Channel<String>) {
    GlobalScope.launch {
        for (i in 20..25) {
            channel.send("Text_$i")
        }
        channel.close()
    }
}

fun function5(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in 30..35) {
            channel.send(i)
        }
        channel.close()
    }
}

fun function6(channel: Channel<String>) {
    GlobalScope.launch {
        for (i in 40..45) {
            channel.send("Data_$i")
        }
        channel.close()
    }
}

fun function7(channel: Channel<Int>) {
    GlobalScope.launch {
        for (i in channel) {
            println("Received Int: $i")
        }
    }
}

fun function8(channel: Channel<String>) {
    GlobalScope.launch {
        for (i in channel) {
            println("Received String: $i")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<String>()

    val dataProducer1 = DataProducer1(channel1)
    val dataProducer2 = DataProducer2(channel2)
    val dataConsumer = DataConsumer(channel1, channel2)

    launch { dataProducer1.produce() }
    launch { dataProducer2.produce() }
    launch { function3(channel3) }
    launch { function4(channel4) }
    launch { function5(channel5) }
    launch { function6(channel6) }
    launch { function7(channel3) }
    launch { function8(channel4) }

    coroutineScope {
        val result = dataConsumer.consume()
        println("Final Result: ${result.first}, ${result.second}")
    }
}