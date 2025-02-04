/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test874
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Example class
class ExampleClass(val inputChannel: Channel<Int>, val outputChannel: Channel<String>)

// Function 1: Sender function that sends data to a channel
suspend fun sendData(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
}

// Function 2: Function that receives data from a channel and sends it to another channel
suspend fun transformData(inputChannel: Channel<Int>, outputChannel: Channel<String>) {
    for (i in 1..5) {
        val received = inputChannel.receive()
        outputChannel.send("Transformed: $received")
    }
}

// Function 3: Function to initialize and use ExampleClass
fun initializeExampleClass(inputChannel: Channel<Int>, outputChannel: Channel<String>) {
    val exampleClass = ExampleClass(inputChannel, outputChannel)
    runBlocking {
        launch { transformData(exampleClass.inputChannel, exampleClass.outputChannel) }
    }
}

// Function 4: Function to receive data from a channel and print it
suspend fun printData(channel: Channel<String>) {
    for (i in 1..5) {
        println(channel.receive())
    }
}

// Main coroutine to initialize channels and coordinate the functions
fun main(): Unit{
    runBlocking {
        val inputChannel1 = Channel<Int>()
        val outputChannel1 = Channel<String>()
        val inputChannel2 = Channel<Int>()
        val outputChannel2 = Channel<String>()

        // Launching coroutines
        launch { sendData(inputChannel1) }
        launch { initializeExampleClass(inputChannel1, outputChannel1) }
        launch { printData(outputChannel1) }

        // Using another set of channels for different operations
        launch { sendData(inputChannel2) }
        launch { transformData(inputChannel2, outputChannel2) }
        launch { printData(outputChannel2) }
    }
}

class RunChecker874: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}