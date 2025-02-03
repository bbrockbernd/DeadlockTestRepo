/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test730
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker1 {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    suspend fun sendNumbers() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }

    suspend fun receiveStrings() {
        for (j in 1..5) {
            val msg = channel2.receive()
            println("Worker1 received: $msg")
        }
    }
}

class Worker2 {
    val channel3 = Channel<String>()
    val channel4 = Channel<Int>()

    suspend fun sendStrings() {
        for (i in 1..5) {
            channel3.send("Message $i")
        }
    }

    suspend fun receiveNumbers() {
        for (j in 1..5) {
            val num = channel4.receive()
            println("Worker2 received: $num")
        }
    }
}

fun main(): Unit= runBlocking {
    val worker1 = Worker1()
    val worker2 = Worker2()

    launch {
        worker1.sendNumbers()
        delay(100)
        for (i in 1..5) {
            worker2.channel4.send(i)
        }
    }

    launch {
        delay(100)
        worker2.sendStrings()
        for (i in 1..5) {
            val num = worker1.channel1.receive()
            println("Main received number: $num")
        }
    }

    launch {
        worker2.receiveNumbers()
    }

    launch {
        delay(100)
        for (i in 1..5) {
            worker1.channel2.send("Message $i")
        }
        worker1.receiveStrings()
    }
}

class RunChecker730: RunCheckerBase() {
    override fun block() = main()
}