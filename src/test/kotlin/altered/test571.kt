/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test571
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel: Channel<Int>, private val receiverChannel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
        receiverChannel.send(1) // Send signal to indicate production is done
    }
}

class ClassB(private val channel: Channel<Int>, private val receiverChannel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val v = channel.receive()
           // Process value v
        }
        receiverChannel.send(1) // Send signal to indicate consumption is done
    }
}

class ClassC(private val receiverChannel1: Channel<Int>, private val receiverChannel2: Channel<Int>) {
    suspend fun monitor() {
        // Wait for both signals
        receiverChannel1.receive()
        receiverChannel2.receive()
    }
}

fun runFirstCoroutine(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            ClassA(channel1, channel2).produce()
        }
    }
}

fun runSecondCoroutine(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            ClassB(channel1, channel2).consume()
        }
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()

runBlocking {
    launch { ClassC(channel2, channel3).monitor() }
    runFirstCoroutine(channel1, channel2)
    runSecondCoroutine(channel1, channel2)
    channel3.send(2)
    channel3.receive() // Optional to prevent exiting before monitor finishes
}

class RunChecker571: RunCheckerBase() {
    override fun block() = main()
}