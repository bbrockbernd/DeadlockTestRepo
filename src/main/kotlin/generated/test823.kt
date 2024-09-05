/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.generated.test823
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelClassA {
    val channel = Channel<Int>()
    suspend fun functionA() {
        channel.send(1)
        channel.receive()
    }
}

class ChannelClassB {
    val channel = Channel<Int>()
    suspend fun functionB(channelA: Channel<Int>) {
        channel.send(channelA.receive())
    }
}

class ChannelClassC {
    val channel = Channel<Int>()
    suspend fun functionC(channelB: Channel<Int>) {
        channel.send(channelB.receive())
        channelB.send(channel.receive())
    }
}

fun mainFunction() = runBlocking {
    val classA = ChannelClassA()
    val classB = ChannelClassB()
    val classC = ChannelClassC()

    launch {
        classA.functionA()
    }
    
    launch {
        classB.functionB(classA.channel)
    }

    launch {
        classC.functionC(classB.channel)
    }

    classC.channel.send(42)
}