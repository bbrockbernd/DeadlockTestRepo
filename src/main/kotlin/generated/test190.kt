/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 7 different coroutines
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
package org.example.generated.test190
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>(2)
    
    fun sendToChannelA(value: Int) = runBlocking {
        channelA.send(value)
    }

    suspend fun receiveFromChannelA() = channelA.receive()
}

class ClassB {
    val channelB = Channel<String>()

    fun sendToChannelB(value: String) = runBlocking {
        channelB.send(value)
    }

    suspend fun receiveFromChannelB() = channelB.receive()
}

class ClassC {
    val channelC = Channel<Double>(5)
    
    fun sendToChannelC(value: Double) = runBlocking {
        channelC.send(value)
    }

    suspend fun receiveFromChannelC() = channelC.receive()
}

fun function1(channel: Channel<Int>) = runBlocking {
    launch {
        repeat(3) {
            channel.send(it)
        }
    }
}

fun function2(channel: Channel<String>) = runBlocking {
    launch {
        repeat(3) {
            channel.send("Message $it")
        }
    }
}

fun function3(channel: Channel<Double>) = runBlocking {
    launch {
        listOf(1.1, 2.2, 3.3).forEach {
            channel.send(it)
        }
    }
}

fun main(): Unit = runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    launch {
        function1(classA.channelA)
        repeat(3) {
            println("Received from ChannelA: ${classA.receiveFromChannelA()}")
        }
    }

    launch {
        function2(classB.channelB)
        repeat(3) {
            println("Received from ChannelB: ${classB.receiveFromChannelB()}")
        }
    }

    launch {
        function3(classC.channelC)
        repeat(3) {
            println("Received from ChannelC: ${classC.receiveFromChannelC()}")
        }
    }

    val channelD = Channel<Long>()
    val channelE = Channel<Float>()
    val channelF = Channel<Char>(3)

    launch {
        repeat(3) {
            channelD.send(it.toLong())
        }
        repeat(3) {
            println("Received from ChannelD: ${channelD.receive()}")
        }
    }

    launch {
        listOf(1.1f, 2.2f, 3.3f).forEach {
            channelE.send(it)
        }
        repeat(3) {
            println("Received from ChannelE: ${channelE.receive()}")
        }
    }

    launch {
        listOf('A', 'B', 'C').forEach {
            channelF.send(it)
        }
        repeat(3) {
            println("Received from ChannelF: ${channelF.receive()}")
        }
    }
}