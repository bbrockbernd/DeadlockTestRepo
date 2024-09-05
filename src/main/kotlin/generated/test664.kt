/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 1 different coroutines
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
- lists, arrays or other datastructures
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
package org.example.generated.test664
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChannelManagerA {
    val channelA = Channel<Int>()
    suspend fun provideDataA() {
        channelA.send(10)
    }

    suspend fun consumeDataA(): Int {
        return channelA.receive()
    }
}

class ChannelManagerB {
    val channelB = Channel<Int>()
    suspend fun provideDataB() {
        channelB.send(20)
    }

    suspend fun consumeDataB(): Int {
        return channelB.receive()
    }
}

class ChannelManagerC {
    val channelC = Channel<Int>()
    suspend fun provideDataC() {
        channelC.send(30)
    }

    suspend fun consumeDataC(): Int {
        return channelC.receive()
    }
}

fun functionA(channelManagerA: ChannelManagerA) = runBlocking {
    coroutineScope {
        launch {
            channelManagerA.provideDataA()
        }
        launch {
            println(channelManagerA.consumeDataA())
        }
    }
}

fun functionB(channelManagerB: ChannelManagerB) = runBlocking {
    coroutineScope {
        launch {
            channelManagerB.provideDataB()
        }
        launch {
            println(channelManagerB.consumeDataB())
        }
    }
}

fun functionC(channelManagerC: ChannelManagerC) = runBlocking {
    coroutineScope {
        launch {
            channelManagerC.provideDataC()
        }
        launch {
            println(channelManagerC.consumeDataC())
        }
    }
}

fun functionD() = runBlocking {
    val channelManagerA = ChannelManagerA()
    val channelManagerB = ChannelManagerB()
    val channelManagerC = ChannelManagerC()

    coroutineScope {
        launch { functionA(channelManagerA) }
        launch { functionB(channelManagerB) }
        launch { functionC(channelManagerC) }
    }
}

fun main(): Unit{
    functionD()
}