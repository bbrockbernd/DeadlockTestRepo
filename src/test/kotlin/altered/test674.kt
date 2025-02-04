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
package org.example.altered.test674
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

class ClassA {
    val channelA = Channel<Int>(5)
    suspend fun sendToChannelA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromChannelA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<Int>()
    suspend fun sendToChannelB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromChannelB(): Int {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Int>()
    suspend fun sendToChannelC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromChannelC(): Int {
        return channelC.receive()
    }
}

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val value = channelA.receive()
                channelB.send(value + 1)
            }
        }
    }
}

fun function2(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val value = channelB.receive()
                channelC.send(value + 1)
            }
        }
    }
}

fun function3(channelC: Channel<Int>, channelD: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val value = channelC.receive()
                channelD.send(value + 1)
            }
        }
    }
}

fun function4(channelD: Channel<Int>, classA: ClassA, classB: ClassB, classC: ClassC) {
    runBlocking {
        launch {
            repeat(5) {
                val value = channelD.receive()
                classA.sendToChannelA(value)
                classB.sendToChannelB(value)
                classC.sendToChannelC(value)
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    val channelD = Channel<Int>(5)

    runBlocking {
        launch {
            repeat(5) {
                channelD.send(it)
            }
        }

        launch {
            function4(channelD, classA, classB, classC)
        }

        coroutineScope {
            launch { function1(classA.channelA, classB.channelB) }
            launch { function2(classB.channelB, classC.channelC) }
            launch { function3(classC.channelC, channelD) }
        }
    }
}

class RunChecker674: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}