/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.generated.test724
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<Int>()

    suspend fun function1() {
        repeat(5) {
            channel1.send(it)
        }
    }

    suspend fun function2() {
        repeat(5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
    }

    suspend fun function3() {
        repeat(5) {
            val value = channel2.receive()
            channel3.send(value + 1)
        }
    }

    suspend fun function4() {
        repeat(5) {
            println("Received from channel3: ${channel3.receive()}")
        }
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()
    
    launch { processor.function1() }
    launch { processor.function2() }
    launch { processor.function3() }
    processor.function4()
}