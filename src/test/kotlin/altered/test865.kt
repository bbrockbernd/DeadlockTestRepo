/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test865
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    fun firstFunction() {
        runBlocking {
            launch {
                for (i in 1..5) {
                    channel1.send(i)
                }
            }
        }
    }
}

class SecondClass {
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()

    fun secondFunction(channel: Channel<Int>) {
        runBlocking {
            launch {
                repeat(5) {
                    val received = channel.receive()
                    channel3.send(received * 2)
                }
            }
        }
    }
}

class ThirdClass {
    fun thirdFunction(channelIn: Channel<Int>, channelOut: Channel<String>) {
        runBlocking {
            launch {
                repeat(5) {
                    val received = channelIn.receive()
                    channelOut.send("Received: $received")
                }
            }
        }
    }

    fun fourthFunction(channel: Channel<String>, channelOut: Channel<String>) {
        runBlocking {
            launch {
                repeat(5) {
                    val received = channel.receive()
                    channelOut.send(received.reversed())
                }
            }
        }
    }
}

fun fifthFunction(channel: Channel<String>) {
    runBlocking {
        launch {
            repeat(5) {
                println(channel.receive())
            }
        }
    }
}

fun main(): Unit{
    val firstClass = FirstClass()
    val secondClass = SecondClass()
    val thirdClass = ThirdClass()

    firstClass.firstFunction()
    secondClass.secondFunction(firstClass.channel1)
    thirdClass.thirdFunction(secondClass.channel3, firstClass.channel2)
    thirdClass.fourthFunction(firstClass.channel2, secondClass.channel4)
    fifthFunction(secondClass.channel4)
}

class RunChecker865: RunCheckerBase() {
    override fun block() = main()
}