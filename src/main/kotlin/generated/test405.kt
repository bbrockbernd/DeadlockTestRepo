/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
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
package org.example.generated.test405
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager(val channel: Channel<Int>) {
    fun initChannels() {
        GlobalScope.launch { sendData1() }
        GlobalScope.launch { sendData2() }
        GlobalScope.launch { receiveData1() }
        GlobalScope.launch { receiveData2() }
    }
    
    private suspend fun sendData1() {
        channel.send(10)
    }
    
    private suspend fun sendData2() {
        channel.send(20)
    }
    
    private suspend fun receiveData1() {
        val data = channel.receive()
        processData1(data)
    }
    
    private suspend fun receiveData2() {
        val data = channel.receive()
        processData2(data)
    }
    
    private fun processData1(data: Int) {
        println("Process Data 1: $data")
    }
    
    private fun processData2(data: Int) {
        println("Process Data 2: $data")
    }
    
    fun start() {
        runBlocking {
            initChannels()
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val channelManager = ChannelManager(channel)
    channelManager.start()
}