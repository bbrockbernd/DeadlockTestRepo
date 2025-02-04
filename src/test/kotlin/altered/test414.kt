/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test414
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    fun sendInt(value: Int) {
        GlobalScope.launch {
            channel1.send(value)
        }
    }

    fun receiveAndSendString() {
        GlobalScope.launch {
            val receivedValue = channel1.receive()
            channel2.send("Received: $receivedValue")
        }
    }
}

class SecondClass(private val channel2: Channel<String>, private val channel3: Channel<Boolean>) {
    fun receiveString() {
        GlobalScope.launch {
            val receivedValue = channel2.receive()
            if (receivedValue.isNotEmpty()) {
                channel3.send(true)
            }
        }
    }
}

class ThirdClass(
    private val channel3: Channel<Boolean>, 
    private val channel4: Channel<Char>, 
    private val channel5: Channel<Char>
) {
    fun receiveBoolean() {
        GlobalScope.launch {
            val receivedValue = channel3.receive()
            if (receivedValue) {
                channel4.send('Y')
            } else {
                channel4.send('N')
            }
        }
    }

    fun transferChar() {
        GlobalScope.launch {
            val receivedChar = channel4.receive()
            channel5.send(receivedChar)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Boolean>()
    val channel4 = Channel<Char>()
    val channel5 = Channel<Char>()

    val firstInstance = FirstClass(channel1, channel2)
    val secondInstance = SecondClass(channel2, channel3)
    val thirdInstance = ThirdClass(channel3, channel4, channel5)

    firstInstance.sendInt(42)
    firstInstance.receiveAndSendString()
    secondInstance.receiveString()
    thirdInstance.receiveBoolean()
    thirdInstance.transferChar()

    val resultChar = channel5.receive()
    println("Final received char: $resultChar")
}

class RunChecker414: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}