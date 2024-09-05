/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.generated.test350
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(ch1: Channel<Int>, ch2: Channel<Int>) = runBlocking {
    launch {
        ch1.send(1)
        println("Sent 1 on ch1 from function1")
        ch2.receive()
        println("Received value on ch2 in function1")
    }
}

fun function2(ch3: Channel<Int>, ch4: Channel<Int>) = runBlocking {
    launch {
        ch3.send(1)
        println("Sent 1 on ch3 from function2")
        ch4.receive()
        println("Received value on ch4 in function2")
    }
}

fun function3(ch5: Channel<Int>, ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>) = runBlocking {
    launch {
        ch5.send(1)
        println("Sent 1 on ch5 from function3")
        ch1.receive()
        println("Received value on ch1 in function3")
        ch4.send(2)
        println("Sent 2 on ch4 from function3")
        ch2.send(2)
        println("Sent 2 on ch2 from function3")
    }
}

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    launch {
        function1(ch1, ch2)
    }
    launch {
        function2(ch3, ch4)
    }
    launch {
        function3(ch5, ch1, ch2, ch3, ch4)
    }
}