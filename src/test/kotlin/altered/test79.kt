/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test79
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<String>)
class C(val channel: Channel<Double>)
class D(val channel: Channel<Long>)
class E(val channel: Channel<Boolean>)

fun functionOne(a: A, d: D) {
    GlobalScope.launch {
        repeat(5) {
            a.channel.send(it)
        }
    }

    GlobalScope.launch {
        repeat(5) {
            d.channel.send(it.toLong())
        }
    }
}

suspend fun functionTwo(b: B, c: C) {
    coroutineScope {
        launch {
            repeat(5) {
                b.channel.send("Message $it")
            }
        }
        
        launch {
            repeat(5) {
                c.channel.send(it * 1.0)
            }
        }
    }
}

suspend fun functionThree(e: E) {
    coroutineScope {
        launch {
            repeat(5) {
                e.channel.send(it % 2 == 0)
            }
        }

        launch {
            repeat(5) {
                e.channel.receive() 
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()
    val channelD = Channel<Long>()
    val channelE = Channel<Boolean>()

    val a = A(channelA)
    val b = B(channelB)
    val c = C(channelC)
    val d = D(channelD)
    val e = E(channelE)

    functionOne(a, d)

    launch {
        functionTwo(b, c)
    }

    launch {
        functionThree(e)
    }

    repeat(5) {
        println("Received from A: ${channelA.receive()}")
        println("Received from D: ${channelD.receive()}")
    }

    repeat(5) {
        println("Received from B: ${channelB.receive()}")
        println("Received from C: ${channelC.receive()}")
    }
    
    repeat(5) {
        println("Received from E: ${channelE.receive()}")
    }

    coroutineContext.cancelChildren()
}

class RunChecker79: RunCheckerBase() {
    override fun block() = main()
}