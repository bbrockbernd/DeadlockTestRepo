/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test962
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

fun function1(example: DeadlockExample) {
    runBlocking {
        launch {
            example.channel1.send(1)
            println("Sent 1 to channel1")
            val received = example.channel2.receive()
            println("Received $received from channel2")
        }
    }
}

fun function2(example: DeadlockExample) {
    runBlocking {
        launch {
            val received = example.channel1.receive()
            println("Received $received from channel1")
            example.channel2.send(2)
            println("Sent 2 to channel2")
        }
    }
}

suspend fun function3(example: DeadlockExample) {
    coroutineScope {
        launch {
            val received = example.channel1.receive()
            println("Coroutine 1 received $received from channel1")
        }
    }
}

suspend fun function4(example: DeadlockExample) {
    coroutineScope {
        launch {
            example.channel1.send(3)
            println("Coroutine 2 sent 3 to channel1")
        }
    }
}

fun main(): Unit{
    val example = DeadlockExample()
    function1(example) // This initiates a send on channel1 and waits to receive on channel2
    function2(example) // This initiates a receive on channel1 and sends on channel2
    runBlocking {
        launch { function3(example) } // This will receive from channel1
        launch { function4(example) } // This will send to channel1
    }
}

class RunChecker962: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}