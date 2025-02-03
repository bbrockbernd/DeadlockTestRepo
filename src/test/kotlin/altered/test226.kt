/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test226
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Classes with some properties
class Task1(val channel: Channel<Int>)
class Task2(val channel: Channel<Int>)
class Task3(val channel: Channel<Int>)
class Task4(val channel: Channel<Int>)
class Task5(val channel: Channel<Int>)

// Functions operating on channels and demonstrating potential deadlock
fun produceData(channel: Channel<Int>) = runBlocking {
    repeat(5) {
        channel.send(it)
    }
}

fun consumeData(channel: Channel<Int>): Int = runBlocking {
    var result = 0
    repeat(5) {
        result += channel.receive()
    }
    result
}

fun coroutineA(channel: Channel<Int>) = runBlocking {
    launch {
        produceData(channel)
    }
}

fun coroutineB(channel: Channel<Int>) = runBlocking {
    launch {
        consumeData(channel)
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>() // Unbuffered channel

    val task1 = Task1(channel)
    val task2 = Task2(channel)
    val task3 = Task3(channel)
    val task4 = Task4(channel)
    val task5 = Task5(channel)

    // Launching coroutines that will create a deadlock
    launch {
        coroutineA(task1.channel)
    }
    launch {
        coroutineB(task2.channel)
    }

    // The following sections use the same unbuffered channel leading to potential deadlocks
    launch {
        coroutineA(task3.channel)
    }
    launch {
        coroutineB(task4.channel)
    }
    coroutineScope {
        launch {
            coroutineA(task5.channel)
        }
        launch {
            coroutineB(task1.channel)
        }
    }
}

class RunChecker226: RunCheckerBase() {
    override fun block() = main()
}