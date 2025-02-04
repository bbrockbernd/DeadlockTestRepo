/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.altered.test136
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(private val ch: Channel<Int>) {
    suspend fun sendNumber(num: Int) {
        ch.send(num)
    }
    
    suspend fun receiveNumber(): Int {
        return ch.receive()
    }
}

class B(private val ch: Channel<String>) {
    suspend fun sendText(text: String) {
        ch.send(text)
    }
    
    suspend fun receiveText(): String {
        return ch.receive()
    }
}

class C(private val ch: Channel<Double>) {
    suspend fun sendDouble(num: Double) {
        ch.send(num)
    }
    
    suspend fun receiveDouble(): Double {
        return ch.receive()
    }
}

fun function1(ch: Channel<Int>) = runBlocking {
    launch {
        ch.send(10)
    }
    launch {
        println(ch.receive())
    }
}

fun function2(a: A) = runBlocking {
    launch {
        a.sendNumber(20)
    }
    launch {
        println(a.receiveNumber())
    }
}

fun function3(b: B) = runBlocking {
    launch {
        b.sendText("Hello")
    }
    launch {
        println(b.receiveText())
    }
}

fun function4(c: C) = runBlocking {
    launch {
        c.sendDouble(3.14)
    }
    launch {
        println(c.receiveDouble())
    }
}

fun function5(ch: Channel<Long>) = runBlocking {
    launch {
        ch.send(100L)
    }
    launch {
        println(ch.receive())
    }
}

fun function6() = runBlocking {
    val ch = Channel<Float>()
    launch {
        ch.send(2.71f)
    }
    launch {
        println(ch.receive())
    }
}

fun function7() = runBlocking {
    val ch = Channel<Boolean>()
    launch {
        ch.send(true)
    }
    launch {
        println(ch.receive())
    }
}

fun function8() = runBlocking {
    val ch = Channel<Char>()
    launch {
        ch.send('K')
    }
    launch {
        println(ch.receive())
    }
}

fun main(): Unit= runBlocking {
    val aChannel = Channel<Int>()
    val a = A(aChannel)
    function2(a)

    val bChannel = Channel<String>()
    val b = B(bChannel)
    function3(b)

    val cChannel = Channel<Double>()
    val c = C(cChannel)
    function4(c)

    function1(Channel())
    function5(Channel())
    function6()
    function7()
    function8()
}

class RunChecker136: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}