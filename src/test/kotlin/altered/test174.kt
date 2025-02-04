/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":7,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test174
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    fun functionA() = runBlocking {
        launch {
            ch1.send(1)
            val result = ch2.receive()
            println("ClassA FunctionA: Received $result")
        }
    }
}

class ClassB(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    fun functionB() = runBlocking {
        launch {
            ch3.send(2)
            val result = ch4.receive()
            println("ClassB FunctionB: Received $result")
        }
    }
}

class ClassC(val ch5: Channel<Int>) {
    fun functionC() = runBlocking {
        launch {
            val result = ch5.receive()
            println("ClassC FunctionC: Received $result")
        }
    }
}

class ClassD(val ch6: Channel<Int>, val ch7: Channel<Int>) {
    fun functionD() = runBlocking {
        launch {
            ch6.send(4)
            val result = ch7.receive()
            println("ClassD FunctionD: Received $result")
        }
    }
}

fun function1(ch: Channel<Int>) = runBlocking {
    launch {
        val result = ch.receive()
        println("Function1: Received $result")
    }
}

fun function2(ch: Channel<Int>) = runBlocking {
    launch {
        ch.send(5)
    }
}

fun function3(ch: Channel<Int>) = runBlocking {
    launch {
        val result = ch.receive()
        println("Function3: Received $result")
    }
}

fun function4(ch: Channel<Int>) = runBlocking {
    launch {
        ch.send(6)
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    val a = ClassA(ch1, ch2)
    val b = ClassB(ch3, ch4)
    val c = ClassC(ch5)
    val d = ClassD(ch6, ch7)

    a.functionA()
    b.functionB()
    c.functionC()
    d.functionD()
    
    function1(ch1)
    function2(ch2)
    function3(ch3)
    function4(ch4)
}

class RunChecker174: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}