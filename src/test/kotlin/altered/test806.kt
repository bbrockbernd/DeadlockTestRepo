/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test806
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelExample(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>, val ch4: Channel<Int>, val ch5: Channel<Int>)

fun function1(example: ChannelExample) = runBlocking {
    launch {
        example.ch1.send(1)
        val received = example.ch2.receive()
        println("Function1 received: $received")
    }
}

fun function2(example: ChannelExample) = runBlocking {
    launch {
        val received = example.ch1.receive()
        println("Function2 received: $received")
        example.ch3.send(2)
    }
}

fun function3(example: ChannelExample) = runBlocking {
    launch {
        val received = example.ch3.receive()
        println("Function3 received: $received")
        example.ch4.send(3)
    }
}

fun function4(example: ChannelExample) = runBlocking {
    launch {
        val received = example.ch4.receive()
        println("Function4 received: $received")
        example.ch2.send(4)
        example.ch5.send(5)
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    
    val example = ChannelExample(ch1, ch2, ch3, ch4, ch5)
    
    launch { function1(example) }
    launch { function2(example) }
    launch { function3(example) }
    launch { function4(example) }
    
    delay(1000) // Artificial delay to allow coroutine execution
}

class RunChecker806: RunCheckerBase() {
    override fun block() = main()
}