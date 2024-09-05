/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test678
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTest {
    private val channel = Channel<Int>()

    fun start() {
        runBlocking {
            coroutineScope {
                launch { function1() }
                launch { function2() }
                launch { function3() }
                launch { function4() }
                launch { function5() }
            }
        }
    }

    suspend fun function1() {
        channel.send(1)
        channel.receive()
    }

    suspend fun function2() {
        channel.send(2)
        channel.receive()
    }

    suspend fun function3() {
        channel.send(3)
        channel.receive()
    }

    suspend fun function4() {
        channel.send(4)
        channel.receive()
    }

    suspend fun function5() {
        channel.send(5)
        channel.receive()
    }
}

fun main(): Unit{
    DeadlockTest().start()
}