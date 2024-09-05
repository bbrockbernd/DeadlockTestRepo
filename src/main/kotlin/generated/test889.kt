/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.generated.test889
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()

fun function1() {
    runBlocking {
        launch {
            channel1.send(1)
            val received = channel2.receive()
            println("Function1 received: $received")
        }
    }
}

fun function2() {
    runBlocking {
        launch {
            val received = channel1.receive()
            println("Function2 received: $received")
            channel3.send(2)
        }
    }
}

fun function3() {
    runBlocking {
        launch {
            val received = channel3.receive()
            println("Function3 received: $received")
            channel4.send(3)
        }
    }
}

fun function4() {
    runBlocking {
        launch {
            val received = channel4.receive()
            println("Function4 received: $received")
            channel2.send(4)
        }
    }
}

fun main(): Unit{
    function1()
    function2()
    function3()
    function4()
}