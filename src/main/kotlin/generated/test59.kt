/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":7,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.generated.test59
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch1: Channel<Int>)
class B(val ch2: Channel<Int>)
class C(val ch3: Channel<Int>)
class D(val ch4: Channel<Int>)
class E(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>, val ch4: Channel<Int>)

fun FunctionA(a: A) = runBlocking {
    launch {
        a.ch1.send(1)
        println("Sent 1 to ch1")
        val received = a.ch1.receive()
        println("Received $received from ch1 in FunctionA")
    }
}

fun FunctionB(b: B) = runBlocking {
    launch {
        b.ch2.send(2)
        println("Sent 2 to ch2")
        val received = b.ch2.receive()
        println("Received $received from ch2 in FunctionB")
    }
}

fun FunctionC(e: E) = runBlocking {
    launch {
        e.ch4.send(4)
        println("Sent 4 to ch4")
        val received = e.ch4.receive()
        println("Received $received from ch4 in FunctionC")
    }
}

fun main(): Unit = runBlocking<Unit> {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()

    val a = A(ch1)
    val b = B(ch2)
    val c = C(ch3)
    val d = D(ch4)
    val e = E(ch1, ch2, ch3, ch4)

    launch { 
        FunctionA(a) 
    }
    launch { 
        FunctionB(b) 
    }
    launch { 
        FunctionC(e) 
    }
    
    launch {
        val received = e.ch1.receive()
        println("Received $received from ch1 in main coroutine")
        e.ch2.send(received + 1)
    }
    
    launch {
        val received = e.ch2.receive()
        println("Received $received from ch2 in main coroutine")
        e.ch3.send(received + 1)
    }

    launch {
        val received = e.ch3.receive()
        println("Received $received from ch3 in main coroutine")
        e.ch4.send(received + 1)
    }
    
    launch {
        val received = e.ch4.receive()
        println("Received $received from ch4 in main coroutine")
        e.ch1.send(received + 1)
    }
    
    delay(10000) // To let the example run for a while
}