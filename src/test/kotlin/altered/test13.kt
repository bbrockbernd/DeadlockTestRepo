/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":1,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 1 different coroutines
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
package org.example.altered.test13
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelOrganizer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(5)
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>(5)
    val channel7 = Channel<Int>(5)

    fun function1() = runBlocking {
        launch {
            for (i in 1..5) {
                channel1.send(i)
            }
            channel1.close()
        }
    }

    suspend fun function2() {
        for (i in channel1) {
            channel2.send(i * 2)
        }
        channel2.close()
    }

    suspend fun function3() {
        for (i in 1..5) {
            channel3.send(i)
        }
        channel3.close()
    }

    suspend fun function4() {
        for (i in channel2) {
            if (i % 2 == 0) {
                channel4.send(i)
            }
        }
        channel4.close()
    }

    suspend fun function5() {
        for (i in channel3) {
            channel5.send(i + 1)
        }
        channel5.close()
    }

    suspend fun function6() {
        for (i in channel4) {
            channel6.send(i * 2)
        }
        channel6.close()
    }

    suspend fun function7() {
        for (i in channel5) {
            channel7.send(i)
        }
        channel7.close()
    }

    suspend fun function8() {
        for (i in channel6) {
            println("Processed value: $i")
        }
    }
}

fun main(): Unit = runBlocking {
    val organizer = ChannelOrganizer()
    organizer.function1()
    launch { organizer.function2() }
    launch { organizer.function3() }
    organizer.function4()
    organizer.function5()
    organizer.function6()
    organizer.function7()
    organizer.function8()
}

class RunChecker13: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}