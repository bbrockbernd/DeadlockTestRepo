/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":7,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.generated.test222
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker1(val input: Channel<Int>, val output: Channel<Int>) {
    fun process() = runBlocking {
        for (x in input) {
            output.send(x + 1)
        }
    }
}

class Worker2(val input: Channel<Int>, val output: Channel<Int>) {
    fun process() = runBlocking {
        for (x in input) {
            output.send(x * 2)
        }
    }
}

class Worker3(val input: Channel<Int>, val output: Channel<Int>) {
    fun process() = runBlocking {
        for (x in input) {
            output.send(x - 1)
        }
    }
}

fun producer(ch1: Channel<Int>, ch2: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..10) {
            ch1.send(i)
            ch2.send(i)
        }
    }
}

fun consumer(ch3: Channel<Int>, ch4: Channel<Int>, result: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..10) {
            val a = ch3.receive()
            val b = ch4.receive()
            result.send(a + b)
        }
    }
}

fun orchestrator(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>, ch6: Channel<Int>,
                 ch7: Channel<Int>, ch8: Channel<Int>) = runBlocking {
    launch { Worker1(ch1, ch5).process() }
    launch { Worker2(ch2, ch6).process() }
    launch { Worker3(ch3, ch7).process() }
    launch { producer(ch1, ch2) }
    launch { consumer(ch5, ch6, ch4) }
    launch { consumer(ch7, ch8, ch3) }

    // This coroutine causes a potential deadlock because it's waiting for data in ch4 and ch8 that may never come
    launch {
        for (i in 1..10) {
            val x = ch4.receive() // Waiting to receive from ch4
            val y = ch8.receive() // Waiting to receive from ch8
            println("Output: ${x + y}")
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    runBlocking {
        orchestrator(ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8)
    }
}