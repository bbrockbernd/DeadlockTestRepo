/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test594
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel.receiveOrNull()
            println("Consumed $value")
            delay(150)
        }
    }
}

suspend fun runTasks(channel: Channel<Int>) {
    val producer = Producer(channel)
    val consumer1 = Consumer(channel)
    val consumer2 = Consumer(channel)
    producer.produce()
    consumer1.consume()
    consumer2.consume()
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch { runTasks(channel) }
    launch { runTasks(channel) }
    launch { runTasks(channel) }
    launch { runTasks(channel) }
}

class RunChecker594: RunCheckerBase() {
    override fun block() = main()
}