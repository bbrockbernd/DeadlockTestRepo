/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 3 different coroutines
- 5 different classes

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
package org.example.altered.test60
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1 {
    val channel = Channel<Int>(1)
    suspend fun produce1() {
        channel.send(1)
    }
}

class Producer2 {
    suspend fun produce2(channel: Channel<Int>) {
        channel.send(2)
    }
}

class Producer3 {
    suspend fun produce3(channel: Channel<Int>) {
        channel.send(3)
    }
}

class Consumer1 {
    suspend fun consume1(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

class Consumer2 {
    suspend fun consume2(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

fun function1(producer: Producer1) = runBlocking {
    launch {
        producer.produce1()
    }
}

fun function2(consumer: Consumer1, channel: Channel<Int>) = runBlocking {
    launch {
        consumer.consume1(channel)
    }
}

fun function3(consumer: Consumer2, channel: Channel<Int>) = runBlocking {
    launch {
        consumer.consume2(channel)
    }
}

fun main(): Unit = runBlocking<Unit> {
    val producer1 = Producer1()
    val producer2 = Producer2()
    val producer3 = Producer3()
    
    val consumer1 = Consumer1()
    val consumer2 = Consumer2()
    val channel = Channel<Int>()

    // Start coroutines
    launch {
        producer2.produce2(channel)
    }
    launch {
        producer3.produce3(channel)
    }
    launch {
        function2(consumer1, producer1.channel)
    }
    
    // Infinite waiting
    function1(producer1)
    function3(consumer2, channel)
}

class RunChecker60: RunCheckerBase() {
    override fun block() = main()
}