/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 2 different coroutines
- 1 different classes

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
package org.example.altered.test864
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>()

    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            println("Produced: $i")
        }
        println("Producer done")
    }

    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Consumed: $value")
        }
        println("Consumer done")
    }

    suspend fun intermediate1() {
        coroutineScope {
            launch { // coroutine C
                produce()
                intermediate2()
            }
        }
    }

    suspend fun intermediate2() {
        coroutineScope {
            launch { // coroutine D
                consume()
            }
        }
    }
}

fun runDeadlockTest() = runBlocking {
    val processor = Processor()
    processor.intermediate1()
}

fun main(): Unit{
    runDeadlockTest()
}

class RunChecker864: RunCheckerBase() {
    override fun block() = main()
}