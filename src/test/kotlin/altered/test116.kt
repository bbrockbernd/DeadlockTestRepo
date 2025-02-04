/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test116
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)

class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)

fun function1(classA: ClassA, classB: ClassB) = runBlocking {
    val channel5 = Channel<Int>(5)
    val channel6 = Channel<Int>()
    
    launch {
        for (i in 1..5) {
            classA.channel1.send(i)
        }
        for (i in 1..5) {
            channel5.send(i)
        }
    }
    
    launch {
        for (i in 1..5) {
            classA.channel2.send(i)
        }
        for (i in 1..5) {
            channel6.send(i)
        }
    }
}

fun function2(channel7: Channel<Int>, channel8: Channel<Int>) = runBlocking {
    val channel5 = Channel<Int>(5)
    
    launch {
        for (i in 1..5) {
            channel7.send(i)
        }
    }
    
    launch {
        for (i in 1..5) {
            val value = channel5.receive()
            channel8.send(value)
        }
    }
}

fun function3() = runBlocking {
    val classA = ClassA(Channel<Int>(), Channel<Int>(5))
    val classB = ClassB(Channel<Int>(), Channel<Int>(5))
    
    function1(classA, classB)
    
    launch {
        for (i in 1..5) {
            val value1 = classA.channel1.receive()
            println("Function3 received from Channel1: $value1")
        }
    }
    
    launch {
        for (i in 1..5) {
            val value2 = classA.channel2.receive()
            println("Function3 received from Channel2: $value2")
        }
    }
}

fun function4() = runBlocking {
    val channel7 = Channel<Int>(5)
    val channel8 = Channel<Int>()

    function2(channel7, channel8)
    
    launch {
        for (i in 1..5) {
            val value7 = channel7.receive()
            println("Function4 received from Channel7: $value7")
        }
    }
    
    launch {
        for (i in 1..5) {
            val value8 = channel8.receive()
            println("Function4 received from Channel8: $value8")
        }
    }
}

fun main(): Unit{
    runBlocking {
        launch {
            function3()
        }
        launch {
            function4()
        }
    }
}

class RunChecker116: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}