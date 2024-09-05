/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.generated.test900
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    suspend fun function1() {
        val value = channelA.receive()
        channelB.send(value)
    }

    suspend fun function2() {
        val value = channelB.receive()
        channelC.send(value)
    }

    suspend fun function3() {
        val value = channelC.receive()
        channelA.send(value)
    }

    fun function4(scope: CoroutineScope) {
        scope.launch {
            function1()
        }
        scope.launch {
            function2()
        }
    }
}

fun main(): Unit= runBlocking {
    val deadlockExample = DeadlockExample()
    launch {
        deadlockExample.function3()
    }
    deadlockExample.function4(this)
    channelA.send(42)
}