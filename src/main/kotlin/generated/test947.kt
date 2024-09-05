/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.generated.test947
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>, handler: Handler) {
        for (i in 1..5) {
            val value = channel.receive()
            handler.process(value)
        }
    }
}

class Handler {
    fun process(value: Int) {
        println("Processed value: $value")
    }
}

fun mainFunction() = runBlocking {
    val channel = Channel<Int>()
    val producer = Producer()
    val consumer = Consumer()
    val handler = Handler()

    launch {
        producer.produce(channel)
        channel.close()
    }

    launch {
        consumer.consume(channel, handler)
    }
}