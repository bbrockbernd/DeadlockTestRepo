/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":8,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 8 different coroutines
- 5 different classes

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
package org.example.generated.test152
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.CoroutineContext

class ClassOne(val channel: Channel<Int>)
class ClassTwo(val channel: Channel<Int>)
class ClassThree(val channel: Channel<Int>)
class ClassFour(val channel: Channel<Int>)
class ClassFive(val channel: Channel<Int>)

fun funcOne(channel: Channel<Int>) {
    runBlocking {
        channel.send(1)
        channel.receive()
    }
}

fun funcTwo(channel: Channel<Int>) {
    runBlocking {
        channel.send(2)
    }
}

fun funcThree(channel: Channel<Int>) {
    runBlocking {
        channel.receive()
    }
}

fun funcFour(channel: Channel<Int>) {
    runBlocking {
        coroutineScope {
            launch {
                channel.receive()
            }
            launch {
                channel.send(4)
            }
        }
    }
}

fun funcFive(channel: Channel<Int>) {
    runBlocking {
        coroutineScope {
            launch {
                channel.send(5)
            }
            launch {
                channel.receive()
            }
        }
    }
}

fun funcSix(channel: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            channel.send(6)
            channel.receive()
        }
    }
}

fun funcSeven(channel: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            channel.send(7)
        }
        launch {
            channel.receive()
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val classOne = ClassOne(channel)
    val classTwo = ClassTwo(channel)
    val classThree = ClassThree(channel)
    val classFour = ClassFour(channel)
    val classFive = ClassFive(channel)

    runBlocking {
        launch { funcOne(classOne.channel) }
        launch { funcTwo(classTwo.channel) }
        launch { funcThree(classThree.channel) }
        launch { funcFour(classFour.channel) }
        launch { funcFive(classFive.channel) }
        launch { funcSix(classOne.channel) }
        launch { funcSeven(classThree.channel) }
        launch { funcOne(classFive.channel) }
    }
}