/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test537
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..10) {
            channel1.send(i)
            delay(100)
        }
        channel1.close()
    }

    suspend fun intermediateProcess() {
        for (element in channel1) {
            channel2.send(element * 2)
        }
        channel2.close()
    }

    suspend fun consume(channel3: Channel<Int>) {
        for (element in channel2) {
            channel3.send(element + 1)
        }
        channel3.close()
    }

    fun visualize(channel3: Channel<Int>) {
        GlobalScope.launch {
            for (element in channel3) {
                println("Processed element: $element")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val processor = Processor(channel1, channel2)

    launch { processor.produce() }
    launch { processor.intermediateProcess() }
    launch { processor.consume(channel3) }

    processor.visualize(channel3)

    delay(5000) // to keep main coroutine alive for a while to see some output
}

class RunChecker537: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}