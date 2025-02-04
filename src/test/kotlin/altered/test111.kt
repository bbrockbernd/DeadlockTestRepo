/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 2 different coroutines
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
package org.example.altered.test111
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<Int>)
class C(val channels: List<Channel<Int>>)
class D(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>)
class E(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>)

fun functionOne(a: A, b: B, c: C) {
    runBlocking {
        launch {
            launch {
                repeat(10) {
                    a.channel.send(it)
                }
            }

            launch {
                for (x in b.channel) {
                    println("B received: $x")
                }
            }

            for (y in c.channels[0]) {
                println("C received: $y from channel 1")
            }
        }
        
        repeat(10) {
            b.channel.send(it)
            c.channels[0].send(it)
        }
        
        a.channel.close()
        b.channel.close()
        c.channels[0].close()
    }
}

fun functionTwo(d: D) {
    runBlocking {
        launch {
            repeat(10) {
                d.inputChannel.send(it)
            }
            d.inputChannel.close()
        }

        launch {
            for (item in d.outputChannel) {
                println("D received: $item")
            }
        }
    }
}

fun functionThree(e: E) {
    runBlocking {
        launch {
            repeat(10) {
                e.outputChannel.send(it)
            }
            e.outputChannel.close()
        }

        launch {
            for (item in e.inputChannel) {
                println("E received: $item")
            }
        }
    }
}

fun main(): Unit {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(Channel.UNLIMITED)
    val channel5 = Channel<Int>(Channel.UNLIMITED)
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val a = A(channel1)
    val b = B(channel2)
    val c = C(listOf(channel3, channel4))
    val d = D(channel5, channel6)
    val e = E(channel6, channel7)

    functionOne(a, b, c)
    functionTwo(d)
    functionThree(e)
}

class RunChecker111: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}