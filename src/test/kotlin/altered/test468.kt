/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test468
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Class1(val ch1: Channel<Int>)
class Class2(val ch2: Channel<String>)
class Class3(val ch3: Channel<Double>)
class Class4(val ch4: Channel<Boolean>)
class Class5

fun function1(ch1: Channel<Int>, ch2: Channel<String>, ch3: Channel<Double>) = runBlocking {
    launch {
        val value1 = ch1.receive()
        println("Received from ch1: $value1")
        ch2.send("Hello")
    }
    launch {
        val value2 = ch3.receive()
        println("Received from ch3: $value2")
    }
}

fun function2(ch2: Channel<String>, ch4: Channel<Boolean>, ch3: Channel<Double>) = runBlocking {
    launch {
        val value1 = ch2.receive()
        println("Received from ch2: $value1")
        ch4.send(true)
    }
    launch {
        ch3.send(42.0)
        println("Sent to ch3: 42.0")
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<String>()
    val ch3 = Channel<Double>()
    val ch4 = Channel<Boolean>()

    val obj1 = Class1(ch1)
    val obj2 = Class2(ch2)
    val obj3 = Class3(ch3)
    val obj4 = Class4(ch4)
    val obj5 = Class5()

    launch {
        ch1.send(100)
        println("Sent to ch1: 100")
        function1(obj1.ch1, obj2.ch2, obj3.ch3)
    }

    launch {
        function2(obj2.ch2, obj4.ch4, obj3.ch3)
    }
}

class RunChecker468: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}