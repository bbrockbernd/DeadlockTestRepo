/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 1 different coroutines
- 4 different classes

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
package org.example.generated.test214
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)
class ClassD(val channel: Channel<Int>)

fun functionOne(a: ClassA, b: ClassB) = CoroutineScope(Dispatchers.Default).launch {
    a.channel.send(1)
    b.channel.receive()
}

fun functionTwo(b: ClassB, c: ClassC) = CoroutineScope(Dispatchers.Default).launch {
    b.channel.send(2)
    c.channel.receive()
}

fun functionThree(c: ClassC, d: ClassD) = CoroutineScope(Dispatchers.Default).launch {
    c.channel.send(3)
    d.channel.receive()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val a = ClassA(channel1)
    val b = ClassB(channel2)
    val c = ClassC(channel3)
    val d = ClassD(channel4)

    functionOne(a, b)
    functionTwo(b, c)
    functionThree(c, d)

    d.channel.send(4)
    a.channel.receive()

    delay(5000) // Just to keep the coroutine alive for observation
}