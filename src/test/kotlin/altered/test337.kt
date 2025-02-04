/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test337
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelExample(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

fun generateData(data: Int): Int {
    return data * 2
}

suspend fun processData(channelExample: ChannelExample, data: Int) {
    val generatedData = generateData(data)
    channelExample.sendData(generatedData)
    val receivedData = channelExample.receiveData()
    println("Processed data: $receivedData")
}

fun main(): Unit= runBlocking {
    val myChannel = Channel<Int>()
    val channelExample = ChannelExample(myChannel)

    launch { processData(channelExample, 1) }
    launch { processData(channelExample, 2) }
    launch { processData(channelExample, 3) }
    launch { processData(channelExample, 4) }
    launch { processData(channelExample, 5) }
    launch { processData(channelExample, 6) }
    launch { processData(channelExample, 7) }
    launch { processData(channelExample, 8) }

    coroutineScope {
        repeat(8) {
            launch {
                val data = myChannel.receive()
                myChannel.send(data + 1)
            }
        }
    }
}


class RunChecker337: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}