/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":1,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
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
package org.example.generated.test318
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelWrapper1(val channel: Channel<Int>)
class ChannelWrapper2(val channel: Channel<Int>)
class ChannelWrapper3(val channel: Channel<Int>)
class ChannelWrapper4(val channel: Channel<Int>)
class ChannelWrapper5(val channel: Channel<Int>)

fun function1(channelWrapper: ChannelWrapper1) {
    runBlocking {
        channelWrapper.channel.send(1)
    }
}

fun function2(channelWrapper: ChannelWrapper2) {
    runBlocking {
        val received = channelWrapper.channel.receive()
        println("Received in function2: $received")
    }
}

fun function3(channelWrapper1: ChannelWrapper1, channelWrapper2: ChannelWrapper2) {
    runBlocking {
        select<Int> {
            channelWrapper1.channel.onReceive {
                channelWrapper2.channel.send(it)
                it
            }
        }
    }
}

fun function4(channelWrapper: ChannelWrapper3) {
    runBlocking {
        channelWrapper.channel.send(2)
    }
}

fun function5(channelWrapper: ChannelWrapper4) {
    runBlocking {
        val received = channelWrapper.channel.receive()
        println("Received in function5: $received")
    }
}

fun function6(channelWrapper1: ChannelWrapper3, channelWrapper2: ChannelWrapper4) {
    runBlocking {
        select<Int> {
            channelWrapper1.channel.onReceive {
                channelWrapper2.channel.send(it)
                it
            }
        }
    }
}

fun function7(channelWrapper5: ChannelWrapper5) {
    runBlocking {
        launch {
            val received = channelWrapper5.channel.receive()
            println("Received in function7: $received")
        }
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()
val channel7 = Channel<Int>()
val channel8 = Channel<Int>()

val wrapper1 = ChannelWrapper1(channel1)
val wrapper2 = ChannelWrapper2(channel2)
val wrapper3 = ChannelWrapper3(channel3)
val wrapper4 = ChannelWrapper4(channel4)
val wrapper5 = ChannelWrapper5(channel5)

fun main(): Unit{
    function1(wrapper1)
    function2(wrapper2)
    function3(wrapper1, wrapper2)
    function4(wrapper3)
    function5(wrapper4)
    function6(wrapper3, wrapper4)

    runBlocking {
        launch {
            channel8.send(3)
        }

        launch {
            val received = channel8.receive()
            wrapper5.channel.send(received)
        }

        function7(wrapper5)
    }
}