/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.generated.test735
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Task1(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channelA.send(i)
        }
        for (i in 6..10) {
            channelB.send(i)
        }
    }
}

class Task2(private val channelA: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun transform() {
        for (i in 1..5) {
            val received = channelA.receive()
            channelC.send(received * 2)
        }
    }
}

class Task3(private val channelB: Channel<Int>, private val channelC: Channel<Int>) {
    suspend fun consume() {
        for (i in 6..10) {
            val received = channelB.receive()
            println("Consumed from B: $received")
        }

        for (i in 1..5) {
            val received = channelC.receive()
            println("Consumed from C: $received")
        }
    }
}

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) = Task1(channelA, channelB)

fun function2(channelA: Channel<Int>, channelC: Channel<Int>) = Task2(channelA, channelC)

fun function3(channelB: Channel<Int>, channelC: Channel<Int>) = Task3(channelB, channelC)

fun function4() = runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    val task1 = function1(channelA, channelB)
    val task2 = function2(channelA, channelC)
    val task3 = function3(channelB, channelC)

    launch { task1.produce() }
    launch { task2.transform() }
    launch { task3.consume() }
}

fun main(): Unit{
    function4()
}