/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.generated.test811
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExampleClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    suspend fun function1() {
        val firstValue = channel1.receive() 
        channel2.send(firstValue + 1) 
        
        val secondValue = channel3.receive() 
        channel1.send(secondValue + 1) 
    }

    suspend fun coroutineBlock() {
        coroutineScope {
            launch {
                val value = channel2.receive() 
                channel3.send(value + 2) 
            }
            launch {
                function1()
            }
            launch {
                channel1.send(10) 
                val finalValue = channel3.receive() 
            }
        }
    }
}

fun main(): Unit{
    runBlocking {
        val example = ExampleClass()
        example.coroutineBlock()
    }
}