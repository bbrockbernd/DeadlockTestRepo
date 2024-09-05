/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.generated.test579
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

class DeadlockTest {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun function1() {
        channelA.send(1)
        val received = channelB.receive()
        println("Function 1 received $received from channelB")
    }

    suspend fun function2() {
        channelB.send(2)
        val received = channelC.receive()
        println("Function 2 received $received from channelC")
    }

    suspend fun function3() {
        channelC.send(3)
        val received = channelD.receive()
        println("Function 3 received $received from channelD")
    }

    suspend fun function4() {
        channelD.send(4)
        val received = channelA.receive()
        println("Function 4 received $received from channelA")
    }
}

fun main(): Unit= runBlocking {
    val test = DeadlockTest()

    coroutineScope {
        launch { test.function1() }
        launch { test.function2() }
        launch { test.function3() }
        launch { test.function4() }
        launch {
            val receivedA = test.channelA.receive()
            test.channelB.send(receivedA)
        }
    }
}

main()