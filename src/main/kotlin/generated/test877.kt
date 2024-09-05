/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 4 different coroutines
- 1 different classes

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
package org.example.generated.test877
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelWrapper(val channel1: Channel<Int>, val channel2: Channel<Int>)

fun function1(channelWrapper: ChannelWrapper, channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channelWrapper.channel1.receive()
        channel3.send(value)
    }
}

fun function2(channelWrapper: ChannelWrapper, channel4: Channel<Int>) = runBlocking {
    launch {
        val value = channelWrapper.channel2.receive()
        channel4.send(value)
    }
}

fun function3(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    launch {
        val value1 = channel3.receive()
        channel4.send(value1)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val wrapper = ChannelWrapper(channel1, channel2)
    
    launch {
        function1(wrapper, channel3)
    }
    
    launch {
        function2(wrapper, channel4)
    }
    
    launch {
        function3(channel3, channel4)
    }
    
    launch {
        // Deadlock: trying to send before receiving from unbuffered channel
        channel1.send(1)
        channel2.send(2)
    }
}