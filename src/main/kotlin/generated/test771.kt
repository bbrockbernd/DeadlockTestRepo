/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.generated.test771
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class FirstClass(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun functionOne() {
        channel1.send(1)
        channel2.receive()
    }
}

class SecondClass(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun functionTwo() {
        channel2.send(2)
        channel1.receive()
    }
}

class ThirdClass(private val firstClass: FirstClass, private val secondClass: SecondClass) {
    suspend fun functionThree() = coroutineScope {
        launch {
            firstClass.functionOne()
        }
        launch {
            secondClass.functionTwo()
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val firstClass = FirstClass(channel1, channel2)
    val secondClass = SecondClass(channel1, channel2)
    val thirdClass = ThirdClass(firstClass, secondClass)

    thirdClass.functionThree()
}