/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 3 different coroutines
- 4 different classes

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
package org.example.altered.test319
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DataClassA(val data: Int)
class DataClassB(val data: String)
class DataClassC(val data: Boolean)
class DataClassD(val data: Double)

fun mainFunction() = runBlocking {
    val channel1 = Channel<DataClassA>()
    val channel2 = Channel<DataClassB>(5)
    val channel3 = Channel<DataClassC>()
    val channel4 = Channel<DataClassD>(3)
    
    launch { firstCoroutine(channel1) }
    launch { secondCoroutine(channel2, channel3) }
    launch { thirdCoroutine(channel4) }
    
    val dataA = DataClassA(42)
    val dataB = DataClassB("Hello")
    val dataC = DataClassC(true)
    val dataD = DataClassD(3.14)
    
    channel1.send(dataA)
    channel2.send(dataB)
    channel3.send(dataC)
    channel4.send(dataD)
}

fun firstCoroutine(channel: Channel<DataClassA>) = runBlocking {
    val receivedData = channel.receive()
    println("First Coroutine received: ${receivedData.data}")
}

fun secondCoroutine(channel2: Channel<DataClassB>, channel3: Channel<DataClassC>) = runBlocking {
    val receivedDataB = channel2.receive()
    val receivedDataC = channel3.receive()
    println("Second Coroutine received: ${receivedDataB.data} and ${receivedDataC.data}")
}

fun thirdCoroutine(channel: Channel<DataClassD>) = runBlocking {
    val receivedData = channel.receive()
    println("Third Coroutine received: ${receivedData.data}")
}

class RunChecker319: RunCheckerBase() {
    override fun block() = main()
}