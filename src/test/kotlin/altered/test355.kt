/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
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
package org.example.altered.test355
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val sendChannel: Channel<String>, val receiveChannel: Channel<String>)
class ClassB(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>)
class ClassC(val sendChannel: Channel<Boolean>, val receiveChannel: Channel<Boolean>)

fun functionA(a: ClassA) = runBlocking {
    a.sendChannel.send("Message from A")
    val response = a.receiveChannel.receive()
    println(response)
}

fun functionB(b: ClassB) = runBlocking {
    b.sendChannel.send(42)
    val response = b.receiveChannel.receive()
    println(response)
}

fun functionC(c: ClassC) = runBlocking {
    c.sendChannel.send(true)
    val response = c.receiveChannel.receive()
    println(response)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Boolean>()
    val channel6 = Channel<Boolean>()

    val a = ClassA(channel1, channel2)
    val b = ClassB(channel3, channel4)
    val c = ClassC(channel5, channel6)

    launch {
        functionA(a)
    }
    launch {
        functionB(b)
    }
    launch {
        functionC(c)
    }

    a.receiveChannel.send("Reply from main to A")
    b.receiveChannel.send(84)
    c.receiveChannel.send(false)

    val msg = a.sendChannel.receive()
    println(msg)

    val num = b.sendChannel.receive()
    println(num)

    val bool = c.sendChannel.receive()
    println(bool)
}

class RunChecker355: RunCheckerBase() {
    override fun block() = main()
}