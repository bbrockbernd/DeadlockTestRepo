/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
- 1 different coroutines
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
package org.example.altered.test420
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
}

class ClassB {
    val channel2 = Channel<String>()
}

class ClassC {
    val channel3 = Channel<Double>()
}

class ClassD {
    val channel4 = Channel<Long>()
}

class ClassE {
    val channel5 = Channel<Boolean>()
}

fun function1(channel: Channel<Int>) = GlobalScope.launch {
    channel.send(42)
}

fun function2(channel: Channel<String>) = GlobalScope.launch {
    channel.send("Hello, World!")
}

fun function3(channel: Channel<Double>) = GlobalScope.launch {
    channel.send(3.14)
}

suspend fun function4(channel: Channel<Long>) = coroutineScope {
    launch {
        channel.send(100L)
    }
}

suspend fun function5(channel: Channel<Boolean>) = coroutineScope {
    launch {
        channel.send(true)
    }
}

fun main(): Unit = runBlocking {
    val a = ClassA()
    val b = ClassB()
    val c = ClassC()
    val d = ClassD()
    val e = ClassE()

    val channel6 = Channel<Char>()
    val channel7 = Channel<Float>()
    
    function1(a.channel1)
    function2(b.channel2)
    function3(c.channel3)
    
    launch { function4(d.channel4) }
    launch { function5(e.channel5) }

    launch {
        channel6.send('K')
        val intReceived = a.channel1.receive()
        val stringReceived = b.channel2.receive()
        val doubleReceived = c.channel3.receive()
        val longReceived = d.channel4.receive()
        val booleanReceived = e.channel5.receive()
        val charReceived = channel6.receive()

        println("Received values: $intReceived, $stringReceived, $doubleReceived, $longReceived, $booleanReceived, $charReceived")
    }
}

class RunChecker420: RunCheckerBase() {
    override fun block() = main()
}