/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 7 different coroutines
- 2 different classes

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
package org.example.generated.test275
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch: Channel<Int>)
class B(val ch: Channel<Int>)

fun func1(a: A, b: B, ch3: Channel<Int>) {
    runBlocking {
        launch {
            ch3.send(a.ch.receive())
            b.ch.send(ch3.receive())
        }
    }
}

fun func2(a: A, ch4: Channel<Int>, ch5: Channel<Int>) {
    runBlocking {
        launch {
            ch4.send(ch5.receive())
            ch5.send(a.ch.receive())
        }
    }
}

fun func3(b: B, ch1: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        launch {
            ch1.send(b.ch.receive())
            ch2.send(ch1.receive())
        }
    }
}

fun func4(ch1: Channel<Int>, ch4: Channel<Int>) {
    runBlocking {
        launch {
            ch4.send(ch1.receive())
        }
    }
}

fun func5(ch2: Channel<Int>, ch5: Channel<Int>) {
    runBlocking {
        launch {
            ch5.send(ch2.receive())
        }
    }
}

fun func6(ch3: Channel<Int>, ch6: Channel<Int>) {
    runBlocking {
        launch {
            ch6.send(ch3.receive())
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    
    val a = A(ch1)
    val b = B(ch2)
    
    runBlocking {
        launch { func1(a, b, ch3) }
        launch { func2(a, ch4, ch5) }
        launch { func3(b, ch1, ch2) }
        launch { func4(ch1, ch4) }
        launch { func5(ch2, ch5) }
        launch { func6(ch3, ch6) }
    }
}