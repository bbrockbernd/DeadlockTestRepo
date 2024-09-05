/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":7,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.generated.test297
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager1 {
    val channel1 = Channel<Int>()
}

class ChannelManager2 {
    val channel2 = Channel<Int>()
}

class ChannelManager3 {
    val channel3 = Channel<Int>(1)
}

class ChannelManager4 {
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
}

val manager1 = ChannelManager1()
val manager2 = ChannelManager2()
val manager3 = ChannelManager3()
val manager4 = ChannelManager4()

fun function1() = runBlocking {
    val job1 = launch {
        manager1.channel1.send(1)
    }
    job1.invokeOnCompletion {
        println("Job 1 completed")
    }
    function4()
}

fun function2() = runBlocking {
    coroutineScope {
        val job2 = launch {
            manager2.channel2.send(manager1.channel1.receive())
        }
        job2.invokeOnCompletion {
            println("Job 2 completed")
        }
    }
}

fun function3() = runBlocking {
    coroutineScope {
        val job3 = launch {
            manager4.channel4.send(manager2.channel2.receive())
        }
        job3.invokeOnCompletion {
            println("Job 3 completed")
        }
    }
}

fun function4() {
    runBlocking {
        function1()
        function2()
        function7()
    }
}

fun function5() = runBlocking {
    coroutineScope {
        val job4 = launch {
            manager3.channel3.send(manager4.channel4.receive())
        }
        job4.invokeOnCompletion {
            println("Job 4 completed")
        }
    }
}

fun function6() = runBlocking {
    coroutineScope {
        val job5 = launch {
            manager3.channel3.send(manager4.channel5.receive())
        }
        job5.invokeOnCompletion {
            println("Job 5 completed")
        }
    }
}

fun function7() = runBlocking {
    coroutineScope {
        val job6 = launch {
            manager4.channel5.send(manager3.channel3.receive())
        }
        job6.invokeOnCompletion {
            println("Job 6 completed")
        }
    }
}

fun main(): Unit{
    runBlocking {
        launch { function5() }
        launch { function6() }
        launch { function1() }
        launch { function2() }
        launch { function3() }
        launch { function4() }
        launch { function7() }
    }
}