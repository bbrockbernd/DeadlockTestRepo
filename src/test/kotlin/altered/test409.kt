/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 6 different coroutines
- 0 different classes

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
package org.example.altered.test409
import org.example.altered.RunCheckerBase
```
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun producer(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun consumer(channel: Channel<Int>) {
    val receivedValue = channel.receive()
    println("Received: $receivedValue")
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { producer(channel1, 1) }
    launch { producer(channel2, 2) }
    launch { consumer(channel1) }
    launch { consumer(channel2) }

    launch {
        val value3 = 3
        producer(channel3, value3)
        consumer(channel3)
    }

    launch {
        val value4 = 4
        producer(channel4, value4)
        consumer(channel4)
    }
}
```

class RunChecker409: RunCheckerBase() {
    override fun block() = main()
}