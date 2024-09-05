/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.generated.test931
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun generateNumbers(channel: Channel<Int>) = GlobalScope.launch {
    repeat(5) {
        channel.send(it)
    }
    channel.close()
}

fun processNumbers(channel: Channel<Int>, resultChannel: Channel<Int>) = GlobalScope.launch {
    for (num in channel)
        resultChannel.send(num * num)
    resultChannel.close()
}

fun printNumbers(resultChannel: Channel<Int>) = GlobalScope.launch {
    for (num in resultChannel)
        println("Received number: $num")
}

fun main(): Unit= runBlocking {
    val numberChannel = Channel<Int>()
    val resultChannel = Channel<Int>()

    generateNumbers(numberChannel)
    processNumbers(numberChannel, resultChannel)
    printNumbers(resultChannel)
    
    delay(2000)  // Ensure the program runs long enough to see results
}