/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 5 different coroutines
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
package org.example.generated.test441
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val outChannel: Channel<Int>, val inChannel: Channel<Int>)
class B(val outChannel: Channel<String>)
class C(val outChannel: Channel<Double>, val inChannel: Channel<Double>)
class D(val inChannel: Channel<Double>)
class E(val outChannel: Channel<Character>, val inChannel: Channel<Character>)

fun func0(outChannel: Channel<Int>, inChannel: Channel<Int>, msg: Int) {
    GlobalScope.launch {
        outChannel.send(msg)
        inChannel.receive()
    }
}

fun func1(outChannel: Channel<String>, msg: String) {
    GlobalScope.launch {
        outChannel.send(msg)
    }
}

suspend fun func2(outChannel: Channel<Double>, inChannel: Channel<Double>, msg: Double) {
    coroutineScope {
        launch {
            outChannel.send(msg)
            inChannel.receive()
        }
    }
}

fun func3(outChannel: Channel<Character>, inChannel: Channel<Character>, msg: Character) {
    GlobalScope.launch {
        outChannel.send(msg)
        inChannel.receive()
    }
}

suspend fun func4(chan1: Channel<Int>, chan2: Channel<String>) {
    coroutineScope {
        launch {
            chan1.send(42)
        }
        launch {
            chan2.send("Hello")
        }
    }
}

fun func5(chan1: Channel<Double>, chan2: Channel<Double>) {
    GlobalScope.launch {
        chan1.receive()
        chan2.receive()
    }
}

fun func6(outChannel: Channel<Double>, msg: Double) {
    GlobalScope.launch {
        outChannel.send(msg)
    }
}

suspend fun func7(chan: Channel<Character>, msg: Character) {
    coroutineScope {
        launch {
            chan.send(msg)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelInt1 = Channel<Int>()
    val channelInt2 = Channel<Int>()
    val channelString = Channel<String>()
    val channelDouble1 = Channel<Double>()
    val channelDouble2 = Channel<Double>()
    val channelCharacter1 = Channel<Character>()
    val channelCharacter2 = Channel<Character>()

    val objA = A(channelInt1, channelInt2)
    val objB = B(channelString)
    val objC = C(channelDouble1, channelDouble2)
    val objD = D(channelDouble2)
    val objE = E(channelCharacter1, channelCharacter2)

    func0(objA.outChannel, objA.inChannel, 100)
    func1(objB.outChannel, "Message")
    func2(objC.outChannel, objC.inChannel, 20.5)
    func3(objE.outChannel, objE.inChannel, 'X')
    func4(channelInt1, channelString)
    func5(channelDouble1, channelDouble2)
    func6(channelDouble1, 30.0)
    func7(channelCharacter1, 'A')
}