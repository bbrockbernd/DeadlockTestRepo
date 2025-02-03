/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":1,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 1 different coroutines
- 2 different classes

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
package org.example.altered.test603
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)
class B(val channel3: Channel<Int>, val channel4: Channel<Int>, val channel5: Channel<Int>)

fun deadlockInducer(a: A, b: B) {
    runBlocking {
        launch {
            val value1 = a.channel1.receive()
            val value2 = b.channel3.receive()
            a.channel2.send(value1 + value2)
        }

        launch {
            val value3 = b.channel4.receive()
            b.channel5.send(value3)
        }

        launch {
            val value4 = b.channel5.receive()
            b.channel3.send(value4)
        }
        
        coroutineScope {
            launch {
                a.channel1.send(42)
                b.channel4.send(1)
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    
    val a = A(channel1, channel2)
    val b = B(channel3, channel4, channel5)
    
    deadlockInducer(a, b)
}

class RunChecker603: RunCheckerBase() {
    override fun block() = main()
}